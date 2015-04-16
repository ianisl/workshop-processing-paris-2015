// Paramètres
int agentCount = 1000;
float agentSize = 5;
int timeIntervalBetweenUpdates = 300; // Intervalle temporel (en millisecondes) entre deux mises à jour de l'angle des agents

// Variables
ArrayList<Agent> agents;
int timeOfLastUpdate; // Stockage du moment où a eu lieu la dernière mise à jour de l'angle des agents

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
    int currentTime = millis(); // Très important: stocker le temps dans une variable au début de 'draw' (n'appeler qu'une seule fois 'millis')
    if (currentTime - timeOfLastUpdate > timeIntervalBetweenUpdates) 
    {
        timeOfLastUpdate = currentTime;
        for (Agent a : agents)
        {
            a.angle = random(2 * PI); // Si une durée suffisante s'est écoulée : définition d'un nouvel angle
            a.updatePosition();
        }
    }
    else 
    {
        for (Agent a : agents)
        {
            a.updatePosition(); // Sinon : conservation du même angle
        }
    }
    background(255);
    stroke(0);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }    
}