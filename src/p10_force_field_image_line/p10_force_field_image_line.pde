// Paramètres
int agentCount = 3;
float agentAlpha = 90;
float fieldIntensity = 0.0005;

// Variables
ImageField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new ImageField(fieldIntensity, "mies.jpg");
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
    noFill();
    stroke(0, agentAlpha);
    beginShape();
    for (Agent a : agents)
    {
        vertex(a.position.x, a.position.y); // Création d'une ligne brisée (pas de points de contrôle)
    }
    endShape();
}