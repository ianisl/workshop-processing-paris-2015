import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.processing.wiki.triangulate.*; 
import controlP5.*; 

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

public class Uta_Stansburiana extends PApplet {

// Workshop FAB2015 - Ianis Lallemand

// Uta Stansburiana Domination
// Gr\u00e9goire Belot (RHABDOLOGY) & Jean-Baptiste Berthez\u00e8ne (CHEERI)
// www.facebook.com/rhabdology
// www.cheeriparis.com




int agentCount = 1000;
int r, g, b;
float distCollision = 28;
float distCollisionSqr;
float distMax = 100;
float distMaxSqr;
float agentStepSize = 1;
float nbLez1, nbLez2, nbLez3 = 0;
float distP1P2, distP2P3, distP3P1;
PImage trameR, trameG, trameB;
float t1, t2, t3;
float scaleR, scaleG, scaleB;
ArrayList<Agent> agents;
ArrayList<Triangle> trianglesR;
ArrayList<Triangle> trianglesG;
ArrayList<Triangle> trianglesB;
ArrayList<PVector> pointsR;
ArrayList<PVector> pointsG;
ArrayList<PVector> pointsB;

ControlP5 cp5;

public void setup() 
{
  size(1200, 800, P2D);
  smooth();
  background(0, 0, 100);
  noFill();
  textureWrap(REPEAT);
  trameR = loadImage("T1.jpg");
  trameG = loadImage("T2.jpg");
  trameB = loadImage("T3.jpg");
  t1 = 701;
  t2 = 702;
  t3 = 703;

  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0));
  cp5.addSlider("distCollision", 2, 200).setValue(distCollision).linebreak();
  cp5.addSlider("distMax", 2, 200).setValue(distMax);

  colorMode(HSB, 360, 100, 100);

  pointsR = new ArrayList<PVector>();
  pointsG = new ArrayList<PVector>();
  pointsB = new ArrayList<PVector>();
  trianglesR = new ArrayList<Triangle>();
  trianglesG = new ArrayList<Triangle>();
  trianglesB = new ArrayList<Triangle>();
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++) {
    Agent a = new Agent();
    a.stepSize = agentStepSize;
    agents.add(a);
  }
  distCollisionSqr = distCollision * distCollision;
  distMaxSqr = distMax * distMax;
}

public void draw() {
  pointsR.clear(); 
  pointsG.clear(); 
  pointsB.clear(); 
  background(167, 100, 100);
  denombrement();
  majTypeLezard();
  majPosLezard();
}

public void majTypeLezard() {
  float distA1A2;
  for (int i = 0; i < agentCount; i++) {
    for (int j = i+1; j < agentCount; j++) {
      Agent a1 =agents.get(i);
      Agent a2 =agents.get(j);
      distA1A2 = sq(a1.position.x-a2.position.x)+sq(a1.position.y-a2.position.y);
      if (distA1A2<distCollisionSqr ) {
        if (a1.lezard == 0 && a2.lezard == 1) {
          a1.lezard =1;
        } else if (a1.lezard==0 && a2.lezard == 2) {
          a2.lezard =0;
        } else if (a1.lezard==1 && a2.lezard == 0) {
          a2.lezard =1;
        } else if (a1.lezard==1 && a2.lezard == 2) {
          a1.lezard =2;
        } else if (a1.lezard==2 && a2.lezard == 0) {
          a1.lezard =0;
        } else if (a1.lezard==2 && a2.lezard == 1) {
          a2.lezard =2;
        }
      }
    }
  }
}

public void majPosLezard() {
  for (Agent a : agents) {
    a.angle = random(2 * PI);
    a.updatePosition();
    switch(a.lezard) {
    case 0: 
      pointsR.add(a.position);
      break;
    case 1: 
      pointsG.add(a.position);
      break;
    default:
      pointsB.add(a.position);
      break;
    }
  }
  
  if (pointsR.size() > 3) {
    trianglesR = Triangulate.triangulate(pointsR);
  }
  if (pointsG.size() > 3) {
    trianglesG = Triangulate.triangulate(pointsG);
  }
  if (pointsB.size() > 3) {
    trianglesB = Triangulate.triangulate(pointsB);
  }

  triangulationLez1(); 
  triangulationLez2(); 
  triangulationLez3();
}

public void triangulationLez1() {
  strokeWeight(0.1f);
  beginShape(TRIANGLES);
  texture(trameR);
  noStroke();
  fill(r, 20);
  for (Triangle t : trianglesR) {
    distP1P2 = sq(t.p1.x-t.p2.x)+sq(t.p1.y-t.p2.y);
    distP2P3 = sq(t.p2.x-t.p3.x)+sq(t.p2.y-t.p3.y);
    distP3P1 = sq(t.p3.x-t.p1.x)+sq(t.p3.y-t.p1.y);
    if (max(distP1P2, distP2P3, distP3P1)<distMaxSqr) {
      vertex(t.p1.x, t.p1.y, 0, 0);
      vertex(t.p2.x, t.p2.y, scaleR, 0);
      vertex(t.p3.x, t.p3.y, 0, scaleR);
    }
  }
  endShape();
}

public void triangulationLez2() {
  beginShape(TRIANGLES);
  texture(trameG);
  noStroke();
  fill(g, 20);
  for (Triangle t : trianglesG) {
    distP1P2 = sq(t.p1.x-t.p2.x)+sq(t.p1.y-t.p2.y);
    distP2P3 = sq(t.p2.x-t.p3.x)+sq(t.p2.y-t.p3.y);
    distP3P1 = sq(t.p3.x-t.p1.x)+sq(t.p3.y-t.p1.y);
    if (max(distP1P2, distP2P3, distP3P1)<distMaxSqr) {
      vertex(t.p1.x, t.p1.y, 0, 0);
      vertex(t.p2.x, t.p2.y, scaleG, 0);
      vertex(t.p3.x, t.p3.y, 0, scaleG);
    }
  }
  endShape();
}

public void triangulationLez3() {
  beginShape(TRIANGLES);
  texture(trameB);
  noStroke();
  fill(b, 20);
  for (Triangle t : trianglesB) {
    distP1P2 = sq(t.p1.x-t.p2.x)+sq(t.p1.y-t.p2.y);
    distP2P3 = sq(t.p2.x-t.p3.x)+sq(t.p2.y-t.p3.y);
    distP3P1 = sq(t.p3.x-t.p1.x)+sq(t.p3.y-t.p1.y);
    if (max(distP1P2, distP2P3, distP3P1)<distMaxSqr) {
      vertex(t.p1.x, t.p1.y, 0, 0);
      vertex(t.p2.x, t.p2.y, scaleB, 0);
      vertex(t.p3.x, t.p3.y, 0, scaleB);
    }
  }
  endShape();
}

public void denombrement() {
  for (Agent a : agents) {
    if (a.lezard == 0) {
      nbLez1++;
    } else if (a.lezard == 1) {
      nbLez2++;
    } else if (a.lezard == 2) {
      nbLez3++;
    }
  }
  nbLez1 = map(nbLez1, 0, agentCount, 100, 10);
  nbLez2 = map(nbLez2, 0, agentCount, 100, 10);
  nbLez3 = map(nbLez3, 0, agentCount, 100, 10);
  int N = width/20;
  scaleR = nbLez1*(2+cos(millis()*TAU/t1))*N*1.5f;
  scaleG = nbLez2*(2+cos(millis()*TAU/t2))*N;
  scaleB = nbLez3*(2+cos(millis()*TAU/t3))*N/2;
}

public void distCollision(float value) {
  distCollisionSqr = value * value;
}
public void distMax(float value) {
  distMaxSqr = value * value;
}

class Agent 
{
  // Attributs de la classe
  PVector position; // Position de l'agent
  PVector previousPosition; // Stockage de la position pr\u00e9c\u00e9dente (pour le dessin)
  float stepSize; // Incr\u00e9ment de d\u00e9placement (= vitesse de base de l'agent)
  float angle; // Angle de d\u00e9placement de l'agent
  boolean isPositionResetWhenOutside; // Permet d'activer ou non la r\u00e9initialisation de la position de l'agent lorsqu'il quitte l'espace du sketch
  int lezard;
  boolean isActif;
  // Le constucteur par d\u00e9faut de la classe
  Agent() 
  {
    position = new PVector(random(width), random(height)); // Position al\u00e9atoire
    previousPosition = position.get(); // Attention \u00e0 bien copier le PVector avec la m\u00e9thode 'get()';
    angle = random(2 * PI); // Angle al\u00e9atoire
    stepSize =1;
    isPositionResetWhenOutside = true;
    isActif = true;
    lezard=floor(random(2.9f));
  }

  // Un autre constructeur dont l'argument 'position' permet de sp\u00e9cifier une position
  Agent(PVector position)
  {
    this(); // Appel du constructeur par d\u00e9faut pour initialiser tous les attributs
    this.position = position; // Mise \u00e0 jour de l'attribut position avec l'argument 'position' pass\u00e9 au constructeur. Comme l'argument du constructeur et l'attribut ont le m\u00eame nom, on identifie l'attribut en le faisant pr\u00e9c\u00e9der de 'this.'
    previousPosition = position.get();
  }
  // Un autre constructeur dont l'argument 'position' permet de sp\u00e9cifier une position
  Agent(int lezard)
  {
    this(); // Appel du constructeur par d\u00e9faut pour initialiser tous les attributs
    this.lezard = lezard; // Mise \u00e0 jour de l'attribut position avec l'argument 'position' pass\u00e9 au constructeur. Comme l'argument du constructeur et l'attribut ont le m\u00eame nom, on identifie l'attribut en le faisant pr\u00e9c\u00e9der de 'this.'
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

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Uta_Stansburiana" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
