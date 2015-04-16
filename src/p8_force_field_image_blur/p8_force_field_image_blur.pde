// Paramètres
int agentCount = 1000;
float agentSize = 1.5;
float agentAlpha = 90;
float fieldIntensity = 0.005;
float blurLevel = 20;

// Variables
ImageField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new ImageField(fieldIntensity, "mies.jpg");
    field.blur(blurLevel); // Application d'un flou à l'image pour obtenir des trajectoires plus fluides. Plus la valeur est élevée, plus le calcul sera long à effectuer
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
    stroke(0, agentAlpha);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }    
}