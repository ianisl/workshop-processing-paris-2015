import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Camille_Boulouis_Cartes_2 extends PApplet {



// Param\u00e8tres
int agentCount = 1000;
int timeIntervalBetweenUpdates = 1000;
float agentSize = 0.5f;
float agentAlpha = 50;
float fieldIntensityA = 0.2f;
float fieldIntensityB = 0.2f;
float agentStepSize = 1;
float refreshAlpha = 50;
float refreshFramePeriodicity = 5;
float blurLevel = 0;

// Variables
ControlP5 cp5;
ImageField fieldA;
ImageField fieldB;
ArrayList<Agent> agentsA; //d\u00e9claration
ArrayList<Agent> agentsB;
int timeOfLastUpdate;




public void setup() 
{
  size(800, 800);
  smooth();
  background(255);
  fieldA = new ImageField(fieldIntensityA, "carte_2.jpg"); 
  fieldB = new ImageField(fieldIntensityB, "carte_1.jpg"); 
  fieldA.blur(blurLevel);
  fieldB.blur(blurLevel);

  agentsA = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize;
    a.stepSize = agentSize;
    agentsA.add(a);
  }
  agentsB = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize;
    a.stepSize = agentSize;
    agentsB.add(a);
  }
  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0)); // sinon la couleur du texte est blanche
  cp5.addSlider("agentAlpha", 0, 255).linebreak(); // les chiffres indiquent le min et max // et de mettre les sliders \u00e0 la ligne
  cp5.addSlider("refreshAlpha", 0, 255).linebreak();
  cp5.addSlider("agentSize", 1, 50).linebreak();  
  cp5.addSlider("fieldIntensityA", 0, 1).setValue(fieldIntensityA).linebreak();
  cp5.addSlider("fieldIntensityB", 0, 1).setValue(fieldIntensityB).linebreak();
  cp5.addSlider("noiseScaleA", 1, 500).plugTo(fieldA).linebreak();
  cp5.addSlider("noiseScaleB", 1, 500).plugTo(fieldB).linebreak();
  cp5.addSlider("agentStepSize", 0, 30).setValue(agentStepSize);

  timeOfLastUpdate = millis();
}

public void draw() 
{

    for (Agent a : agentsA)
    {
      a.angle = fieldA.getBrightness(a.position); // Utilisation de la luminosit\u00e9 de l'image \u00e0 la position de l'agent comme nouvelle valeur de l'angle
      a.updatePosition();
    }

    for (Agent a : agentsB)
    {
      a.angle = fieldB.getBrightness(a.position); // Utilisation de la luminosit\u00e9 de l'image \u00e0 la position de l'agent comme nouvelle valeur de l'angle
      a.updatePosition();
    }
  stroke(255, 0, 0, agentAlpha);
  strokeWeight(agentSize);

  for (Agent a : agentsA)
  {
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
  stroke(0, 0, 255, agentAlpha);
  strokeWeight(agentSize);

  for (Agent a : agentsB)
  {
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
}

//cr\u00e9ation d'une m\u00e9thode pour le contr\u00f4le P5 avec value
public void fieldIntensityA(float value)
{
   fieldA.fieldIntensity = value; 
}
public void fieldIntensityB(float value)
{
   fieldB.fieldIntensity = value; 
}


public void agentStepSize(float value)
{
   for (Agent a : agentsA)
  {
    a.stepSize = value;
  }
    for (Agent a : agentsB)
  {
    a.stepSize = value;
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
    float blurlevel = 5;
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
        image.filter(BLUR, blurlevel);
    }
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Camille_Boulouis_Cartes_2" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
