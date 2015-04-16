// Paramètres
int agentCount = 1000;
float agentSize = 1.5;
float agentAlpha = 90;
float agentStepSize = 10;
float fieldIntensity = 10;
float noiseScale = 300;

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new PerlinNoiseField(fieldIntensity, noiseScale); // Création d'un nouveau champ de force
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        a.stepSize = agentStepSize; // Modification de l'attribut 'stepSize' de l'agent que l'on vient de créer
        agents.add(a);
    }
}

void draw() 
{
    for (Agent a : agents)
    {
        a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit à la position de l'agent comme nouvelle valeur de l'angle
        a.updatePosition();
    }    
    stroke(0, agentAlpha);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }    
}