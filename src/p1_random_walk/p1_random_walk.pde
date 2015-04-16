// Paramètres
int agentCount = 1000;
float agentSize = 10;

// Variables
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    agents = new ArrayList<Agent>(); // Utilisation d'un ArrayList pour stocker les agents. ArrayList est un conteneur 'généraliste' : le type d'objets qu'il contient doit être précisé entre les '<>'
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent(); // Création d'un nouvel objet de type 'Agent' 
        agents.add(a); // Ajout de notre objet 'Agent' celui-ci au ArrayList 'agents'
    }
}

void draw() 
{
    for (Agent a : agents)
    {
        a.angle = random(2 * PI); // Marche aléatoire
        a.updatePosition(); // Déplacement de l'agent avec le nouvel angle
    }    
    background(255);
    stroke(0);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }    
}