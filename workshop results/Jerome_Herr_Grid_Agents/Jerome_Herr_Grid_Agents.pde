// Paramètres
int agentCount = 20;
float agentSize = 1.5;
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

void setup() 
{
  size(1200, 800);
  smooth();
  background(255);
  img = loadImage("annalisa-botticelli.jpg");
  field = new PerlinNoiseField(fieldIntensity, noiseScale); // Création d'un nouveau champ de force
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize; // Modification de l'attribut 'stepSize' de l'agent que l'on vient de créer
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

void draw() 
{
  image(img,0,0);
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit à la position de l'agent comme nouvelle valeur de l'angle
    a.updatePosition();
  }    
  stroke(0, agentAlpha);
  strokeWeight(agentSize);
  for (Node n : nodes) {
    n.run();
  }
}

void keyPressed() {
  save(random(23232)+".jpg");
} 