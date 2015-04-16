// Paramètres
int agentCount = 3;
float agentAlpha = 90;
float fieldIntensity = 0.0005;
color polygonColor = color(42, 81, 227, 2);

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
    stroke(0, agentAlpha);
    fill(polygonColor);
    beginShape();
    for (Agent a : agents)
    {
        vertex(a.position.x, a.position.y);
    }
    endShape();
}

void keyPressed()
{
    if (key == 'j') // Attention: guillemets simples obligatoires
    {
        save("recorded/" + getTimestamp() + ".jpg"); // Appuyer sur 'j' pour sauvegarder une image au format jpg
    }
}