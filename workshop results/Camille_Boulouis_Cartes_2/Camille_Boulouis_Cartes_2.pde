import controlP5.*;

// Paramètres
int agentCount = 1000;
int timeIntervalBetweenUpdates = 1000;
float agentSize = 0.5;
float agentAlpha = 50;
float fieldIntensityA = 0.2;
float fieldIntensityB = 0.2;
float agentStepSize = 1;
float refreshAlpha = 50;
float refreshFramePeriodicity = 5;
float blurLevel = 0;

// Variables
ControlP5 cp5;
ImageField fieldA;
ImageField fieldB;
ArrayList<Agent> agentsA; //déclaration
ArrayList<Agent> agentsB;
int timeOfLastUpdate;

void setup() 
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
  cp5.addSlider("agentAlpha", 0, 255).linebreak(); // les chiffres indiquent le min et max // et de mettre les sliders à la ligne
  cp5.addSlider("refreshAlpha", 0, 255).linebreak();
  cp5.addSlider("agentSize", 1, 50).linebreak();  
  cp5.addSlider("fieldIntensityA", 0, 1).setValue(fieldIntensityA).linebreak();
  cp5.addSlider("fieldIntensityB", 0, 1).setValue(fieldIntensityB).linebreak();
  cp5.addSlider("noiseScaleA", 1, 500).plugTo(fieldA).linebreak();
  cp5.addSlider("noiseScaleB", 1, 500).plugTo(fieldB).linebreak();
  cp5.addSlider("agentStepSize", 0, 30).setValue(agentStepSize);

  timeOfLastUpdate = millis();
}

void draw() 
{

    for (Agent a : agentsA)
    {
      a.angle = fieldA.getBrightness(a.position); // Utilisation de la luminosité de l'image à la position de l'agent comme nouvelle valeur de l'angle
      a.updatePosition();
    }

    for (Agent a : agentsB)
    {
      a.angle = fieldB.getBrightness(a.position); // Utilisation de la luminosité de l'image à la position de l'agent comme nouvelle valeur de l'angle
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

//création d'une méthode pour le contrôle P5 avec value
void fieldIntensityA(float value)
{
   fieldA.fieldIntensity = value; 
}
void fieldIntensityB(float value)
{
   fieldB.fieldIntensity = value; 
}


void agentStepSize(float value)
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

