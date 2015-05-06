import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Romain_Chaffal_La_Mouche extends PApplet {



//param\u00e8tres
float agentSize = 0.5f;
int agentCount = 150 ;
float agentAlpha = 100 ;
float refreshAlpha = 10 ;
float distanceMini = 1000 ;
float fieldIntensity = 30;
float noiseScale = 300;
float stepSizePoint = 0.01f;
float agentStepSize = 10;
float amplitude =1 ;
float agentLittleStepSize =0;

//float coefBoule = 100;

// Variables
ArrayList <Agent> agents;
float distance;
Agent point;
float angleVecteur;
PerlinNoiseField field;
ControlP5 cp5;

public void setup() {
  size(700, 700);
  smooth();
  background(255);
  point = new Agent(new PVector(random(width), random(height)));
  agents = new ArrayList<Agent>();
  field = new PerlinNoiseField(fieldIntensity, noiseScale);
  for (int i = 0; i < agentCount; i++) {
    Agent a = new Agent(new PVector(i * width/agentCount, height/2 + 150*sin(5005*(i * width/agentCount) )));
    //Agent a = new Agent();
    agents.add(a);
  }

  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0));
  //cp5.addSlider("agentAlpha", 0, 255).linebreak();
  //cp5.addSlider("refreshAlpha", 0, 255).linebreak();
  //cp5.addSlider("fieldIntensity", 1, 200).plugTo(field).linebreak();
  //cp5.addSlider("noiseScale", 1, 500).plugTo(field).linebreak();
  cp5.addSlider("agentStepSize", 0, 100).setValue(agentStepSize).linebreak();
  //cp5.addSlider("agentLittleStepSize", 0, 1).linebreak();
  //cp5.addSlider("coefBoule", 0, 1000).linebreak();
  cp5.addSlider("stepSizePoint", 0, 0.5f).linebreak();
  cp5.addSlider("agentCount", 2, 1000).linebreak();
  cp5.addSlider("amplitude", 0, 1).linebreak();

  cp5.addSlider("agentSize", 0, 100);
}

public void draw() {

  point.stepSize=stepSizePoint;
  fill(255, refreshAlpha);
  noStroke();
  rect(0, 0, width, height);
  noFill();

  for (Agent a : agents)
  { 
    //distance = PVector.dist(a.position, point.position);
    point.angle = field.getNoiseValue(point.position);

    angleVecteur = atan2(point.position.y-a.position.y, point.position.x-a.position.x);
    pushMatrix();         
    translate(a.position.x, a.position.y);
    rotate(angleVecteur);

    a.updatePosition();
    a.position.x  += cos(amplitude*angleVecteur) * (a.stepSize +agentLittleStepSize)  ;
    a.position.y  += sin(amplitude*angleVecteur) *  (a.stepSize +agentLittleStepSize) ;
    popMatrix();
    point.updatePositionPoint();
  }

  stroke(0, agentAlpha);
  strokeWeight(agentSize);
  //Agent first = agents.get(0);
  //Agent last = agents.get(agents.size() - 1);

  //noStroke();
  //beginShape();
  //curveVertex(first.position.x, first.position.y);
  for (Agent a : agents)
  { 
    //stroke(125, 0, 0);
    //line(point.previousPosition.x, point.previousPosition.y, point.position.x, point.position.y);
   // curveVertex(a.position.x, a.position.y);
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
  //curveVertex(last.position.x, last.position.y);
  //endShape();
}

// Fonction \u00e9coutant les changements apport\u00e9s au slider 'agentStepSize'
public void agentStepSize(float value)
{
  for (Agent a : agents)
  {
    a.stepSize = value;
  }
}

public void agentCount(int value)
{
  int diff = value - agents.size(); // Calcul du nombre d'agents \u00e0 ajouter ou supprimer
  if (diff > 0)
  {
    for (int i = 0; i < diff; i++)
    {
      Agent a = new Agent();
      agents.add(a); // Ajout d'un nombre 'diff' de nouveaux agents
    }
  } else
  {
    diff = -diff; // Attention, diff est ici n\u00e9gatif ! On repasse \u00e0 un nombre positif pour plus de lisibilit\u00e9
    for (int i = 0; i < diff; i++)
    {
      agents.remove(agents.size() - 1); // Suppression d'un nombre 'diff' d'agents en partant de la fin du 'ArrayList'
    }
  }
}

class Agent {

  float stepSize;
  PVector position;
  PVector previousPosition;
  float angle;
  boolean isPositionResetWhenOutside;


  Agent()
  {
    position = new PVector(random(width), random(height));
    previousPosition = position.get();
    stepSize = 0.5f ;
    isPositionResetWhenOutside = true;
    //angle = random(2 * PI);
  }

  //surcharge 
  Agent(PVector position)
  {
    this(); // Appel du constructeur par d\u00e9faut pour initialiser tous les attributs
    this.position = position; // Mise \u00e0 jour de l'attribut position avec l'argument 'position' pass\u00e9 au constructeur. Comme l'argument du constructeur et l'attribut ont le m\u00eame nom, on identifie l'attribut en le faisant pr\u00e9c\u00e9der de 'this.'
    previousPosition = position.get();
  }
  
  public void updatePosition() {
    previousPosition = position.get(); 
    //position.x  -= cos(angle) * stepSize;
   // position.y  -= sin(angle) *  stepSize;

    if ( isPositionResetWhenOutside && isOutsideSketch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }
  
   public void updatePositionPoint() {
    previousPosition = position.get(); 
    position.x  -= cos(angle) * stepSize ;
    position.y  -= sin(angle) *  stepSize ;

    if ( isPositionResetWhenOutside && isOutsideSketch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }

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
        String[] appletArgs = new String[] { "Romain_Chaffal_La_Mouche" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
