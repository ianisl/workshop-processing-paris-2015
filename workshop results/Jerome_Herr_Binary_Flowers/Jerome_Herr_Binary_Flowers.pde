// Paramètres
int agentCount = 1500;
float agentSize = 1.5;
float agentAlpha = 50;
float fieldIntensity = 0.05;
float blurLevel = 10;
float distanceMin=10;
float distanceMax=20;
color colS=#000000, colB=#ffffff;
color myColor=colB;

// Variables
ImageField field;
ArrayList<Agent> agents;

void setup() 
{
  size(1200, 800);
  smooth();
  background(colB);

  field = new ImageField(fieldIntensity, "gun.jpg");
  field.blur(blurLevel);
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    agents.add(a);
  }

}

void draw() 
{
  for (Agent a : agents)
  {
    a.angle = field.getBrightness(a.position);
    a.updatePosition();
  }    
  stroke(myColor,90,90, agentAlpha);
  strokeWeight(agentSize);
  for (Agent a : agents)
  {
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    a.lineBetween();
  }
}

void keyPressed() {
  if (key == 'j') {
    save(random(1234)+".tif");
  }
}
