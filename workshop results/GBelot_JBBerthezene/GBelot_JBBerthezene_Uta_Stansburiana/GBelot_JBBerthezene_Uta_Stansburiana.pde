// Workshop FAB2015 - Ianis Lallemand

// Uta Stansburiana Domination
// Grégoire Belot (RHABDOLOGY) & Jean-Baptiste Berthezène (CHEERI)
// www.facebook.com/rhabdology
// www.cheeriparis.com

import org.processing.wiki.triangulate.*;
import controlP5.*;

int agentCount = 1000;
color r, g, b;
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

void setup() 
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

void draw() {
  pointsR.clear(); 
  pointsG.clear(); 
  pointsB.clear(); 
  background(167, 100, 100);
  denombrement();
  majTypeLezard();
  majPosLezard();
}

void majTypeLezard() {
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

void majPosLezard() {
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

void triangulationLez1() {
  strokeWeight(0.1);
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

void triangulationLez2() {
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

void triangulationLez3() {
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

void denombrement() {
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
  scaleR = nbLez1*(2+cos(millis()*TAU/t1))*N*1.5;
  scaleG = nbLez2*(2+cos(millis()*TAU/t2))*N;
  scaleB = nbLez3*(2+cos(millis()*TAU/t3))*N/2;
}

void distCollision(float value) {
  distCollisionSqr = value * value;
}
void distMax(float value) {
  distMaxSqr = value * value;
}

