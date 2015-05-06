import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.processing.wiki.triangulate.*; 
import java.util.Calendar; 

import org.processing.wiki.triangulate.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Frederic_Briolet_Triangulate extends PApplet {



// Param\u00e8tres
int agentCount = 1000;
float fieldIntensity = 0.03f;
float blurLevel = 1;
float edgeWeight = 1;
float edgeAlpha = 1;
float circleAlpha = 2;
float circleColorHue = 350; 
float circleColorSaturation = 125;
float circleColorBrightness = 100;
float brightnessStep = 0.2f; // Vitesse du d\u00e9grad\u00e9 au cours du temps. L'espace HSB permet de g\u00e9n\u00e9rer des variations autour d'une teinte de base (ici, le bleu).
float saturationStep = -0.1f;

// Variables
ImageField field;
ArrayList<Agent> agents;
ArrayList<PVector> points; // La librairie 'triangulate' travaille sur un ArrayList de 'PVector'
ArrayList<Triangle> triangles; // La librairie renvoie ses r\u00e9sultats sous forme d'une liste de triangles

public void setup() 
{
    size(1000, 1000);
    smooth(8);
    background(0);
    colorMode(HSB, 360, 100, 100);
    field = new ImageField(fieldIntensity, "onionskin.jpg");
    field.blur(blurLevel);
    agents = new ArrayList<Agent>();
    points = new ArrayList<PVector>();
    triangles = new ArrayList<Triangle>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        agents.add(a);
    }
}

public void draw() 
{
    points.clear(); 
    for (Agent a : agents)
    {
        a.angle = field.getBrightness(a.position);
        a.updatePosition();
        points.add(a.position);
    }
    triangles = Triangulate.triangulate(points); // Utilisation de la m\u00e9thode 'triangulate' contenue dans la classe 'Triangulate'. On ne cr\u00e9e pas d'objet : on dit que la m\u00e9thode est 'statique'.
    strokeWeight(edgeWeight);
    noFill();
    beginShape(TRIANGLES); // Il est ici pratique d'utiliser ce mode de dessin pour tracer des triangles
    stroke(255, edgeAlpha);
    for (Triangle t : triangles)
    {
        stroke(color(circleColorHue, circleColorSaturation, circleColorBrightness), circleAlpha);
        vertex(t.p1.x, t.p1.y);
        vertex(t.p2.x, t.p2.y);
        vertex(t.p3.x, t.p3.y);
    }
    endShape();
}

public void keyPressed()
{
    if (key == 'j')
    {
        save("recorded/" + getTimestamp() + ".jpg");
    }
}
class Agent 
{
    // Attributs de la classe
    PVector position; // Position de l'agent
    PVector previousPosition; // Stockage de la position pr\u00e9c\u00e9dente (pour le dessin)
    float stepSize; // Incr\u00e9ment de d\u00e9placement (= vitesse de base de l'agent)
    float angle; // Angle de d\u00e9placement de l'agent
    boolean isPositionResetWhenOutside; // Permet d'activer ou non la r\u00e9initialisation de la position de l'agent lorsqu'il quitte l'espace du sketch

    // Le constucteur par d\u00e9faut de la classe
    Agent() 
    {
        position = new PVector(random(width), random(height)); // Position al\u00e9atoire
        previousPosition = position.get(); // Attention \u00e0 bien copier le PVector avec la m\u00e9thode 'get()';
        angle = random(2 * PI); // Angle al\u00e9atoire
        stepSize = 1;
        isPositionResetWhenOutside = true;
    }

    // Un autre constructeur dont l'argument 'position' permet de sp\u00e9cifier une position
    Agent(PVector position)
    {
        this(); // Appel du constructeur par d\u00e9faut pour initialiser tous les attributs
        this.position = position; // Mise \u00e0 jour de l'attribut position avec l'argument 'position' pass\u00e9 au constructeur. Comme l'argument du constructeur et l'attribut ont le m\u00eame nom, on identifie l'attribut en le faisant pr\u00e9c\u00e9der de 'this.'
        previousPosition = position.get();
    }

    // Une m\u00e9thode de la classe permettant de mettre \u00e0 jour la position de l'agent (en fonction de son angle de d\u00e9placement actuel)
    public void updatePosition() 
    {
        previousPosition = position.get(); // Sauvegarde de la position pr\u00e9c\u00e9dente
        position.x += cos(angle) * stepSize; // L'agent avance sur une distance \u00e9gale \u00e0 'stepSize' \u00e0 partir de sa position actuelle, selon un angle 'angle'
        position.y += sin(angle) * stepSize;
        if (isOutsideSketch() > 0 && isPositionResetWhenOutside) 
        {
            position = new PVector(random(width), random(height)); // Si l'agent sort du sketch, on lui attribue une nouvelle position al\u00e9atoire
            previousPosition = position.get();
        }
    }
    
    // Une m\u00e9thode permettant de v\u00e9rifier si l'agent est sorti des limites de l'espace du sketch (+ marges)
    // La m\u00e9thode renvoie les valeurs suivantes :
    // 0: l'agent n'est pas sorti des limites de l'espace du sketch
    // 1: l'agent est sorti par le haut
    // 2: l'agent est sorti par la droite
    // 3: l'agent est sorti par le bas
    // 4: l'agent est sorti par la gauche
    public int isOutsideSketch()
    {
        if (position.y < 0) 
        {
            return 1;
        } 
        else if (position.x > width) 
        {
            return 2;
        } 
        else if (position.y > height)
        {
            return 3;
        }
        else if (position.x < 0)
        {
            return 4;
        } 
        else
        {
            return 0;
        }
    }
}
class ImageField
{
    float fieldIntensity; // Intensit\u00e9 du champ de force
    PImage image; // Fichier image (g\u00e9om\u00e9trie du champ de force)

    // Le constructeur permet de charger une image \u00e0 partir du chemin d'acc\u00e8s
    // au fichier.
    ImageField(float fieldIntensity, String imagePath) 
    {
        this.fieldIntensity = fieldIntensity;
        this.image = loadImage(imagePath);
    }

    // M\u00e9thode permettant d'obtenir la luminosit\u00e9 de l'image \u00e0 une position donn\u00e9e
    public float getBrightness(PVector position)
    {
        int c = getColor(position);
        return brightness(c) * fieldIntensity;
    }

    // Il peut \u00eatre utile de d\u00e9finir \u00e9galement une m\u00e9thode permettant d'obtenir
    // la couleur de l'image \u00e0 une position donn\u00e9e.
    public int getColor(PVector position)
    {
        return image.get(PApplet.parseInt(position.x/width * image.width), PApplet.parseInt(position.y/height * image.height));
    }

    // M\u00e9thode permettant d'appliquer un flou \u00e0 l'image
    public void blur(float blurLevel)
    {
        image.filter(BLUR, blurLevel);
    }
}


// Cette fonction g\u00e9n\u00e8re un timestamp (information sur la date et l'heure d'ex\u00e9cution)
// sous la forme 2015-04-05_20h-30m-22s
public String getTimestamp() 
{
    Calendar now = Calendar.getInstance();
    return String.format("20%1$ty-%1$tm-%1$td_%1$tHh%1$tMm%1$tSs", now);
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Frederic_Briolet_Triangulate" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
