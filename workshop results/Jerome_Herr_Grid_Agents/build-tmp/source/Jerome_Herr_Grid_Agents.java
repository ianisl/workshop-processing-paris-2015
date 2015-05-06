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

public class Jerome_Herr_Grid_Agents extends PApplet {

// Param\u00e8tres
int agentCount = 20;
float agentSize = 1.5f;
float agentAlpha = 100;
float agentStepSize = 10;
float fieldIntensity = 15;
float noiseScale = 300;
int spaceBetween = 5;
int edge=0;
float maxDist = 100;
float minDist = 0;
PImage img;

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;
ArrayList<Node> nodes;

public void setup() 
{
  size(1200, 800);
  smooth();
  background(255);
  img = loadImage("annalisa-botticelli.jpg");
  field = new PerlinNoiseField(fieldIntensity, noiseScale); // Cr\u00e9ation d'un nouveau champ de force
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize; // Modification de l'attribut 'stepSize' de l'agent que l'on vient de cr\u00e9er
    agents.add(a);
  }

  nodes = new ArrayList<Node>();
  for (int x=edge; x<=width-edge; x+=spaceBetween) {
    for (int y=edge; y<=height-edge; y+=spaceBetween) {
      Node n = new Node(new PVector(x, y));
      nodes.add(n);
    }
  }
}

public void draw() 
{
  image(img,0,0);
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit \u00e0 la position de l'agent comme nouvelle valeur de l'angle
    a.updatePosition();
  }    
  stroke(0, agentAlpha);
  strokeWeight(agentSize);
  for (Node n : nodes) {
    n.run();
  }
}

public void keyPressed() {
  save(random(23232)+".jpg");
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
        stepSize = agentStepSize;
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
      stepSize=agentStepSize;  
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
class Node {

  PVector position_start;
  PVector position_updated;
  float sz=3;
  float sz_upd;
  float m; // multiplicateur
  float t; // time
  boolean inDistance;
  int fc; // frameCount at a certain moment in time
  float incr, timer;

  Node(PVector start) {
    position_start = start;
    position_updated = start;
    sz_upd=sz;
  }

  public void run() {
    update_dist();
    //update_sz();
    display_sz();
    returnToStart();
  }

  public void update_dist() {
    for (Agent a : agents) {
      float distance = dist(a.position.x, a.position.y, position_updated.x, position_updated.y);
      if (distance>minDist && distance<maxDist) {
        m = map(distance, minDist, maxDist, 3, 0);
        incr = map(distance, minDist, maxDist, 0.03f, 0.01f);
        timer = map(distance, minDist, maxDist, 40, 10);
        fc = frameCount;
      } else {
        m = 0;
        //inDistance = false;
      }
      PVector p = PVector.sub(position_updated, a.position);
      p.normalize();
      p.mult(m);
      position_updated = PVector.add(position_updated, p);
    }
  }

  public void returnToStart() {
    if (frameCount>fc+timer) {
      position_updated.lerp(position_start, t);
      if (t<.95f) t+= incr;
    }
  }

  public void update_sz() {
    for (Agent a : agents) {
      float distance = dist(a.position.x, a.position.y, position_updated.x, position_updated.y);
      if (distance<maxDist) {
        sz_upd = map(distance, 0, maxDist, 10, 5);
      } else {
        sz_upd = sz;
      }
    }
  }

  public void display_sz() {
    fill(0);
    noStroke();
    ellipse(position_updated.x, position_updated.y, sz_upd, sz_upd);
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
        String[] appletArgs = new String[] { "Jerome_Herr_Grid_Agents" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
