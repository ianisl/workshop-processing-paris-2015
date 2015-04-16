// Paramètres
int agentCount = 200;
float agentSize = 50;
float agentAlpha = 40;
float fieldIntensity = 20;
float noiseScale = 300;

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new PerlinNoiseField(fieldIntensity, noiseScale);
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
        a.angle = field.getNoiseValue(a.position);
        a.updatePosition();
    }    
    stroke(0, agentAlpha);
    strokeWeight(1);
    noFill();
    for (Agent a : agents)
    {
        ellipse(a.position.x, a.position.y, agentSize, agentSize); // Représentation des agents par des cercles
    }    
}