import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Calendar; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Frederic_Briolet_ImageField_1 extends PApplet {

// Param\u00e8tres
int agentCount = 5000;
float agentSize = 1.5f;
float agentAlpha = 1;
float fieldIntensity = 0.03f;
float blurLevel = 1;
float noiseScale = 100;
float agentStepSize = 2;
int mouseColor = color(255, 0, 0);
int mouseColorOpacity = 15;

// Variables
PerlinNoiseField fieldPerlin;
ImageField field;
ArrayList<Agent> agents;

public void setup() 
{
  size(1000, 765);
  smooth(0);
  background(0);
  field = new ImageField(fieldIntensity, "balls.jpg");
  fieldPerlin = new PerlinNoiseField(fieldIntensity, noiseScale);
  field.blur(blurLevel);
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize;
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
  strokeWeight(agentSize);
  for (Agent a : agents)
  {
    stroke(255, 3);
    if(a.position.x > (mouseX-random(200)) && a.position.x < (mouseX+random(200)) && a.position.y < (mouseY+random(200)) && a.position.y > (mouseY-random(200))){
      stroke(mouseColor, mouseColorOpacity);
      fieldIntensity = random(400);
      a.position.x += 10;
    }
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
  
}

// Interactive 

public void keyPressed() {
  if (key == 'j') {
    save("recorded/" + getTimestamp() + ".jpg");
  }
  if (key == ' ') {
    noiseScale = random(200);
    fieldIntensity = random(0.02f,0.04f);
    background(0);
    setup();
  }
  if (key == 'a') {
    mouseColor = color(0,0,255); // bleu
  }
  if (key == 'z') {
    mouseColor = color(255,0,0); // rouge
  }
  if (key == 'e') {
    mouseColor = color(255,215,0); // or
  }
  if (key == 'r') {
    mouseColor = color(119,181,254); // bleu ciel
  }
  if (key == 't') {
    mouseColor = color(144,40,59); // amarante
  }
  if (key == 'y') {
    mouseColor = color(158,14,64); // pourpre
  }
  if (key == 'u') {
    mouseColor = color(53,122,183); // c\u00e9rul\u00e9en
  }
  if (key == 'i') {
    mouseColor = color(204,85,0); // orange brul\u00e9e
  }
  if (key == 'o') {
    mouseColor = color(100,155,136); // glauque
  }
  if (key == 'p') {
    mouseColor = color(0,142,142); // bleu sarcelle
  }
  if (key == 'q') {
    mouseColor = color(1,121,111); // vert pin
  }
  if (key == 's') {
    mouseColor = color(106,69,93); // colombin
  }
  if (key == 'd') {
    mouseColor = color(239,216,7); // Jaune d'or
  }
  if (key == 'w') {
    mouseColor = color(255,255,255); // Blanc
  }
  if (key == 'x') {
    mouseColor = color(0,0,0); // Noir
    println( mouseColor );
  }
  if (keyCode == UP ) {
    mouseColorOpacity += 2; // opacit\u00e9 couleur + 2
  }
  if (keyCode == DOWN ) {
    mouseColorOpacity -= 2; // opacit\u00e9 couleur - 2
  }
  if (key == 'n') {
    blurLevel += 1; // blurLevel = lissage
    println(blurLevel);
  }
  if (key == 'b') {
    blurLevel -= 1; // blurLevel = lissage;
    println(blurLevel);
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
        position.x += sin(angle) * stepSize; // L'agent avance sur une distance \u00e9gale \u00e0 'stepSize' \u00e0 partir de sa position actuelle, selon un angle 'angle'
        position.y += cos(angle) * stepSize;
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
class PerlinNoiseField
{
    float fieldIntensity; // Intensit\u00e9 du champ de force
    float noiseScale; // 'G\u00e9om\u00e9trie' du bruit

    PerlinNoiseField(float fieldIntensity, float noiseScale) 
    {
        this.fieldIntensity = fieldIntensity;
        this.noiseScale = noiseScale;
    }

    // M\u00e9thode permettant d'obtenir la valeur du champ de force \u00e0 une position donn\u00e9e
    public float getNoiseValue(PVector position)
    {
        return noise(position.x / noiseScale, position.y / noiseScale) * fieldIntensity;
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
        String[] appletArgs = new String[] { "Frederic_Briolet_ImageField_1" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
