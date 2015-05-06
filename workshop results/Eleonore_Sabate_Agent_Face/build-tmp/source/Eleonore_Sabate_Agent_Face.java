import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.processing.wiki.triangulate.*; 
import java.util.Calendar; 

import controlP5.*; 
import org.processing.wiki.triangulate.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Eleonore_Sabate_Agent_Face extends PApplet {



int agentCount = 80;
int pointCount = 100;
float stepSize = 2;
float floor=100;
float coeff = 0.0007f;
float refreshAlpha=22;
int refreshFramePeriodicity = 10 ;
int refreshVisage = 300;
int margin = 50;
boolean expansion = true ;
int compteur=PApplet.parseInt(random(26));

PImage visage ;
int colorTriangle;
float sqDistanceBetweenPointAndAgent;
ArrayList<PVector> points ;
ArrayList<Agent> agents;
ArrayList<Triangle> triangles;
HashMap<Triangle, Integer> triangleColors;
float floorSqr;
float leftLimit, rightLimit, topLimit, bottomLimit ;

public void setup() 
{ 
  size(800, 800);
  //addInputPin("coeff", new FloatPin(0, 0.0009, 0.0004));
  //addInputPin("floor", new FloatPin(30, 150, 0.0004));

  background(255);
  rectMode(CENTER);
  smooth();

  resizeimage();

  points = new ArrayList<PVector>();
  agents = new ArrayList<Agent>();
  triangles = new ArrayList<Triangle>();
  triangleColors = new HashMap<Triangle, Integer>();


  for (int i = 0; i < pointCount; i++)
  {
    PVector p = new PVector(random(leftLimit, visage.width+rightLimit), random(topLimit, visage.height+bottomLimit)); 
    points.add(p);
  }

  for (int i = 0; i < agentCount; i++)
  { 
    Agent a = new Agent();
    a.stepSize = stepSize;
    agents.add(a);
  }

  floorSqr = floor * floor;
  triangles = Triangulate.triangulate(points);
  points.clear();

  for (Triangle t : triangles)
  {
    points.add(t.p1);
    points.add(t.p2);
    points.add(t.p3);
    triangleColors.put(t, visage.get(PApplet.parseInt(t.p1.x/width * visage.width), PApplet.parseInt(t.p1.y/height * visage.height)));
  }
}

public void draw() 
{
  //coeff=getInputPinValue("coeff");
  //floor=getInputPinValue("floor");

  float flipCoin=random(frameCount);
  if (flipCoin <frameCount/100) 
  { 
    expansion=!expansion;
  }

  if (frameCount % refreshVisage == 0)
  {
    refreshVisage=PApplet.parseInt(random(60, 700));
    changeimage();
  }

  if (frameCount % refreshFramePeriodicity == 0)
  {
    fill(255, refreshAlpha);
    noStroke();
    rect(width/2, height/2, width, height);
  }

  for (Agent a : agents)
  { 
    for (PVector p : points) 
    {
      a.angle=random(2*PI);
      a.updatePosition();
      sqDistanceBetweenPointAndAgent = sq(a.position.x-p.x) + sq(a.position.y-p.y);

      if ( sqDistanceBetweenPointAndAgent < floorSqr)
      {
        if (expansion) 
        {
          p.x = p.x + coeff * (a.position.x - p.x);
          p.y = p.y + coeff * (a.position.y - p.y);
        } else 
        {
          p.x = p.x - coeff * (a.position.x - p.x);
          p.y = p.y - coeff * (a.position.y - p.y);
        }
      }
    }
  }
  beginShape(TRIANGLES);
  for (int i =0; i < triangles.size (); i++)
  {
    Triangle t = triangles.get(i);
    colorTriangle = triangleColors.get(t);

    stroke(colorTriangle);
    fill(colorTriangle);
    vertex(t.p1.x, t.p1.y);
    vertex(t.p2.x, t.p2.y);
    vertex(t.p3.x, t.p3.y);
  }
  endShape();
}

public void changeimage()
{
  //save("recorded/" + getTimestamp() + ".jpg");
  compteur=PApplet.parseInt(random(26));

  background(255);

  resizeimage();

  points.clear();
  triangles.clear();
  triangleColors.clear();

  triangleColors = new HashMap<Triangle, Integer>();

  for (int i = 0; i < pointCount; i++)
  {
    PVector p = new PVector(random(leftLimit, visage.width+rightLimit), random(topLimit, visage.height+bottomLimit)); 
    points.add(p);
  }


  triangles = Triangulate.triangulate(points);
  points.clear();

  for (Triangle t : triangles)
  {
    points.add(t.p1);
    points.add(t.p2);
    points.add(t.p3);
    triangleColors.put(t, visage.get(PApplet.parseInt(t.p1.x/width * visage.width), PApplet.parseInt(t.p1.y/height * visage.height)));
  }
}

public void resizeimage() {

  visage=loadImage("image"+compteur+".jpg");

  if (visage.width > visage.height)

  { 
    visage.resize(width-(margin*2), 0);

    float customargin=(height-visage.height)/2;

    image(visage, margin, customargin);

    leftLimit = margin +random(visage.width/4);
    rightLimit = margin-random(visage.width/4);
    topLimit = customargin + random(visage.height/4);
    bottomLimit = customargin - random(visage.height/4);
  } else 
  {   
    visage.resize(0, height-(margin*2));

    float customargin = (width-visage.width)/2;
    image(visage, customargin, margin);

    leftLimit = customargin +random(visage.width/4);
    rightLimit = customargin-random(visage.width/4);
    topLimit = margin + random(visage.height/4);
    bottomLimit = margin - random(visage.height/4);
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


// Cette fonction g\u00e9n\u00e8re un timestamp (information sur la date et l'heure d'ex\u00e9cution)
// sous la forme 2015-04-05_20h-30m-22s
public String getTimestamp() 
{
    Calendar now = Calendar.getInstance();
    return String.format("20%1$ty-%1$tm-%1$td_%1$tHh%1$tMm%1$tSs", now);
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Eleonore_Sabate_Agent_Face" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
