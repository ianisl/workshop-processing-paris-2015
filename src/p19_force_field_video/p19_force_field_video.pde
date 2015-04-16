// Paramètres
int agentCount = 4000;
float agentSize = 1.5;
float agentStepSize = 10;
float agentAlpha = 10;
float fieldIntensity = 0.01;

// Variables
VideoField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new VideoField(fieldIntensity, this);
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        a.stepSize = agentStepSize;
        agents.add(a);
    }
}

void draw() 
{
    field.update();
    if (field.isImageLoaded)
    {
        for (Agent a : agents)
        {
            a.angle = field.getBrightness(a.position);
            a.updatePosition();
        }    
        strokeWeight(agentSize);
        stroke(0, agentAlpha);
        for (Agent a : agents)
        {
            stroke(field.getColor(a.position), agentAlpha); // Récupération de la couleur de l'image capturée à partir de la caméra
            line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
        }
    }
}