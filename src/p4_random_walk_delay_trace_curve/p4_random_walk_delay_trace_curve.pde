// Paramètres
int agentCount = 5;
int timeIntervalBetweenUpdates = 1000;
float agentAlpha = 15;

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
    Agent first = agents.get(0);
    Agent last = agents.get(agents.size() - 1);
    noFill();
    stroke(0, agentAlpha);
    beginShape(); // Début de la définition du tracé
    curveVertex(first.position.x, first.position.y); // Premier point de contrôle (guide le début de la courbe)
    for (Agent a : agents)
    {
        curveVertex(a.position.x, a.position.y); // Ajout des points de la courbe
    }
    curveVertex(last.position.x, last.position.y); // Deuxième point de contrôle (guide la fin de la courbe)
    endShape(); // Fin de la définition du tracé
}