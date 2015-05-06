import controlP5.*;

// Paramètres
int agentCount = 1000;
float agentSize = 1.5;
float agentAlpha = 90;
float agentStepSize = 5;
float fieldIntensity = 10;
float noiseScale = 300;
float rayon = 200;
float spiral = 0.2;
int count=0;
float x;
float y;
long nSeed = (long) random(1000);

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;
ControlP5 cp5;
ArrayList<Agent> agentsToRemove;

void setup() 
{
  size(1200, 800);
  smooth();
  background(255);
  field = new PerlinNoiseField(fieldIntensity, noiseScale); // Création d'un nouveau champ de force
  x = width/2;
  y = height/2;
  agents = new ArrayList<Agent>();
  agentsToRemove = new ArrayList<Agent>();

  createStuff();
  cp5 = new ControlP5(this);
  cp5.setColorCaptionLabel(color(0));
  cp5.addSlider("noiseScale", 1, 500).plugTo(field).linebreak();
  cp5.addSlider("fieldIntensity", 1, 20).plugTo(field).linebreak();
  cp5.addSlider("rayon", 25, height*.4).linebreak();
  cp5.addSlider("agentAlpha", 0, 255).linebreak();
  cp5.addSlider("spiral", 0, 1).linebreak();
  cp5.addSlider("agentStepSize", 1, 10).linebreak();
  cp5.addSlider("x", 0, width).linebreak();
  cp5.addSlider("y", 0, height).linebreak();
  cp5.addButton("nSeed");
}

void draw() 
{
  count=0;
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit à la position de l'agent comme nouvelle valeur de l'angle
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

void keyPressed() {
  if (key == 'j') {
    save(random(23233)+".jpg");
  }
  if (key == 'n') {
    background(255);
    agents.clear();
    createStuff();
  }
}

void createStuff() {
  noiseSeed(nSeed);
  background(255);
  agents.clear();
  for (int i = 0; i < agentCount; i++)
  {
    float offSet = TWO_PI/agentCount*i;
    float r = map(i, 0, agentCount-1, 1.0-spiral, 1.0);
    float _x =x+cos(offSet)*rayon*r;
    float _y = y+sin(offSet)*rayon*r;
    Agent a = new Agent(new PVector(_x, _y));  
    a.stepSize = agentStepSize; // Modification de l'attribut 'stepSize' de l'agent que l'on vient de créer
    agents.add(a);
  }
}

void controlEvent(ControlEvent theEvent) {
  if (theEvent.isFrom(cp5.getController("nSeed"))) {
    nSeed=(long)random(10000);
  }
  agents.clear();
  createStuff();
}

