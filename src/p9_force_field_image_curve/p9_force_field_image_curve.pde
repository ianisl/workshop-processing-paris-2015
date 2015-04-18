// Paramètres
int agentCount = 3;
float agentAlpha = 90;
float fieldIntensity = 0.0005;
float blurLevel = 20;

// Variables
ImageField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new ImageField(fieldIntensity, "x.jpg");
    field.blur(20);
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
    Agent first = agents.get(0);
    Agent last = agents.get(agents.size() - 1);
    noFill();
    stroke(0, agentAlpha);
    beginShape();
    curveVertex(first.position.x, first.position.y);
    for (Agent a : agents)
    {
        curveVertex(a.position.x, a.position.y);
    }
    curveVertex(last.position.x, last.position.y);
    endShape();
}