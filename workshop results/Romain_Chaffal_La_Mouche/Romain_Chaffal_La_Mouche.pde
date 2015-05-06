import controlP5.*;

//paramètres
float agentSize = 0.5;
int agentCount = 150 ;
float agentAlpha = 100 ;
float refreshAlpha = 10 ;
float distanceMini = 1000 ;
float fieldIntensity = 30;
float noiseScale = 300;
float stepSizePoint = 0.01;
float agentStepSize = 10;
float amplitude =1 ;
float agentLittleStepSize =0;

// Variables
ArrayList <Agent> agents;
float distance;
Agent point;
float angleVecteur;
PerlinNoiseField field;
ControlP5 cp5;

void setup() {
  size(700, 700);
  smooth();
  background(255);
  point = new Agent(new PVector(random(width), random(height)));
  agents = new ArrayList<Agent>();
  field = new PerlinNoiseField(fieldIntensity, noiseScale);
  for (int i = 0; i < agentCount; i++) {
    Agent a = new Agent(new PVector(i * width/agentCount, height/2 + 150*sin(5005*(i * width/agentCount) )));
    agents.add(a);
  }

  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0));
  cp5.addSlider("agentStepSize", 0, 100).setValue(agentStepSize).linebreak();
  cp5.addSlider("stepSizePoint", 0, 0.5).linebreak();
  cp5.addSlider("agentCount", 2, 1000).linebreak();
  cp5.addSlider("amplitude", 0, 1).linebreak();

  cp5.addSlider("agentSize", 0, 100);
}

void draw() {

  point.stepSize=stepSizePoint;
  fill(255, refreshAlpha);
  noStroke();
  rect(0, 0, width, height);
  noFill();

  for (Agent a : agents)
  { 
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
  for (Agent a : agents)
  { 
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
}

// Fonction écoutant les changements apportés au slider 'agentStepSize'
void agentStepSize(float value)
{
  for (Agent a : agents)
  {
    a.stepSize = value;
  }
}

void agentCount(int value)
{
  int diff = value - agents.size(); // Calcul du nombre d'agents à ajouter ou supprimer
  if (diff > 0)
  {
    for (int i = 0; i < diff; i++)
    {
      Agent a = new Agent();
      agents.add(a); // Ajout d'un nombre 'diff' de nouveaux agents
    }
  } else
  {
    diff = -diff; // Attention, diff est ici négatif ! On repasse à un nombre positif pour plus de lisibilité
    for (int i = 0; i < diff; i++)
    {
      agents.remove(agents.size() - 1); // Suppression d'un nombre 'diff' d'agents en partant de la fin du 'ArrayList'
    }
  }
}

