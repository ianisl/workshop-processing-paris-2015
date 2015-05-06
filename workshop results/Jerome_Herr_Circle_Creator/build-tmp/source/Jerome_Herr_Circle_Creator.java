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

public class Jerome_Herr_Circle_Creator extends PApplet {



// Param\u00e8tres
int agentCount = 1000;
float agentSize = 1.5f;
float agentAlpha = 90;
float agentStepSize = 5;
float fieldIntensity = 10;
float noiseScale = 300;
float rayon = 200;
float spiral = 0.2f;
int count=0;
float x;
float y;
long nSeed = (long) random(1000);

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;
ControlP5 cp5;
ArrayList<Agent> agentsToRemove;

public void setup() 
{
  size(1200, 800);
  smooth();
  background(255);
  field = new PerlinNoiseField(fieldIntensity, noiseScale); // Cr\u00e9ation d'un nouveau champ de force
  x = width/2;
  y = height/2;
  agents = new ArrayList<Agent>();
  agentsToRemove = new ArrayList<Agent>();

  createStuff();
  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0));
  cp5.addSlider("noiseScale", 1, 500).plugTo(field).linebreak();
  cp5.addSlider("fieldIntensity", 1, 20).plugTo(field).linebreak();
  cp5.addSlider("rayon", 25, height*.4f).linebreak();
  cp5.addSlider("agentAlpha", 0, 255).linebreak();
  cp5.addSlider("spiral", 0, 1).linebreak();
  cp5.addSlider("agentStepSize", 1, 10).linebreak();
  cp5.addSlider("x", 0, width).linebreak();
  cp5.addSlider("y", 0, height).linebreak();
  cp5.addButton("nSeed");
}

public void draw() 
{
  count=0;
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit \u00e0 la position de l'agent comme nouvelle valeur de l'angle
    a.updatePosition();
  }    
  stroke(0, agentAlpha);
  strokeWeight(agentSize);
  agentsToRemove.clear(); 
  for (Agent a : agents) {
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    if (a.isOutsideSketch()>0) agentsToRemove.add(a);
  }
  for (Agent a : agentsToRemove)
  {
    agents.remove(a);
  }
}

public void keyPressed() {
  if (key == 'j') {
    save(random(23233)+".jpg");
  }
  if (key == 'n') {
    background(255);
    agents.clear();
    createStuff();
  }
}

public void createStuff() {
  noiseSeed(nSeed);
  background(255);
  agents.clear();
  for (int i = 0; i < agentCount; i++)
  {
    float offSet = TWO_PI/agentCount*i;
    float r = map(i, 0, agentCount-1, 1.0f-spiral, 1.0f);
    float _x =x+cos(offSet)*rayon*r;
    float _y = y+sin(offSet)*rayon*r;
    Agent a = new Agent(new PVector(_x, _y));  
    a.stepSize = agentStepSize; // Modification de l'attribut 'stepSize' de l'agent que l'on vient de cr\u00e9er
    agents.add(a);
  }
}

public void controlEvent(ControlEvent theEvent) {
  if (theEvent.isFrom(cp5.getController("nSeed"))) {
    nSeed=(long)random(10000);
  }
  agents.clear();
  createStuff();
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
    isPositionResetWhenOutside = false;
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
    if (isOutsideSketch() > 0) {
      if (isPositionResetWhenOutside) {
        position = new PVector(random(width), random(height)); // Si l'agent sort du sketch, on lui attribue une nouvelle position al\u00e9atoire
        previousPosition = position.get();
      }
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
    } else if (position.x > width) 
    {
      return 2;
    } else if (position.y > height)
    {
      return 3;
    } else if (position.x < 0)
    {
      return 4;
    } else
    {
      return 0;
    }
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
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Jerome_Herr_Circle_Creator" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
