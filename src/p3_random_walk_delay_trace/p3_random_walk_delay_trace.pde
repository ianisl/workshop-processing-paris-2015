// Paramètres
int agentCount = 1000;
float agentSize = 5;
int timeIntervalBetweenUpdates = 100;
float agentAlpha = 5; // Ne pas rafraîchir l'écran et dessiner en transparence permet d'obtenir des effets de trace

// Variables
ArrayList<Agent> agents;
int timeOfLastUpdate;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        agents.add(a);
    }
    timeOfLastUpdate = millis();
}

void draw() 
{
    int currentTime = millis();
    if (currentTime - timeOfLastUpdate > timeIntervalBetweenUpdates) 
    {
        timeOfLastUpdate = currentTime;
        for (Agent a : agents)
        {
            a.angle = random(2 * PI);
            a.updatePosition();
        }
    }
    else 
    {
        for (Agent a : agents)
        {
            a.updatePosition();
        }
    }
    stroke(0, agentAlpha);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }    
}