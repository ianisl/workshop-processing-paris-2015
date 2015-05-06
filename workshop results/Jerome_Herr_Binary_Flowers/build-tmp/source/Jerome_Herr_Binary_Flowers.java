import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Jerome_Herr_Binary_Flowers extends PApplet {

// Param\u00e8tres
int agentCount = 1500;
float agentSize = 1.5f;
float agentAlpha = 50;
float fieldIntensity = 0.05f;
float blurLevel = 10;
float distanceMin=10;
float distanceMax=20;
int colS=0xff000000, colB=0xffffffff;
int myColor=colB;

// Variables
ImageField field;
ArrayList<Agent> agents;

public void setup() 
{
  size(1200, 800);
  smooth();
  background(colB);

  field = new ImageField(fieldIntensity, "gun.jpg");
  field.blur(blurLevel);
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    agents.add(a);
  }

}

public void draw() 
{
  for (Agent a : agents)
  {
    a.angle = field.getBrightness(a.position);
    a.updatePosition();
  }    
  stroke(myColor,90,90, agentAlpha);
  strokeWeight(agentSize);
  for (Agent a : agents)
  {
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    a.lineBetween();
  }
}

public void keyPressed() {
  if (key == 'j') {
    save(random(1234)+".tif");
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
    
    public void lineBetween() {
    for (int i=0; i<agents.size(); i++) {
      Agent other = agents.get(i);
      float distance = position.dist(other.position);
      if (distance>distanceMin && distance < distanceMax) {
        stroke(myColor,90,90,10);
        line(position.x, position.y, other.position.x, other.position.y);
      }
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
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Jerome_Herr_Binary_Flowers" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
