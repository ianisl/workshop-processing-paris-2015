// Paramètres
int agentCount = 500;
float fieldIntensity = 10;
float noiseScale = 30;
float circleRadius = 200;
float circleLineWeight = 1.5;
float circleAlpha = 20;

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
    float angleStep = 2 * PI / agentCount;
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent(new PVector(width / 2 + cos(i * angleStep) * circleRadius, height / 2 + sin(i * angleStep) * circleRadius)); // Initialisation avec une position fixée
        a.isPositionResetWhenOutside = false; // Les agents ne sont pas ramenés dans l'espace du sketch lorsqu'ils en sortent
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
    stroke(0, circleAlpha);
    strokeWeight(circleLineWeight);
    noFill();
    beginShape();
    for (Agent a : agents)
    {
        curveVertex(a.position.x, a.position.y);
    }
    if (agents.size() > 3)
    {
        curveVertex(agents.get(0).position.x, agents.get(0).position.y); // Ajout (une seconde fois) des trois premiers points du cercle pour le fermer
        curveVertex(agents.get(1).position.x, agents.get(1).position.y);
        curveVertex(agents.get(2).position.x, agents.get(2).position.y);
    }
    endShape();
}

void keyPressed()
{
    if (key == 'j')
    {
        save("recorded/" + getTimestamp() + ".jpg");
    }
}