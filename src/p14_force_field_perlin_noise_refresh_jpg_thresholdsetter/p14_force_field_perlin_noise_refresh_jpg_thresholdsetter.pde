import controlP5.*;

// Paramètres
int agentCount = 1000;
float agentSize = 1;
float agentAlpha = 35;
float agentStepSize = 1;
float fieldIntensity = 24;
float refreshAlpha = 0;
float maxNoiseScale = 300; // Utilisation d'une valeur aléatoire de noiseScale dont on définit ici le maximum

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;
ControlP5 cp5;
MinimThresholdSetter minimThresholdSetter;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new PerlinNoiseField(fieldIntensity, random(maxNoiseScale));
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        a.stepSize = agentStepSize;
        agents.add(a);
    }
    cp5 = new ControlP5(this);
    cp5.setColorCaptionLabel(color(0));
    cp5.addSlider("agentAlpha", 0, 255).linebreak();
    cp5.addSlider("refreshAlpha", 0, 255).linebreak();
    cp5.addSlider("agentSize", 1, 50).linebreak();
    cp5.addSlider("fieldIntensity", 1, 30).plugTo(field).linebreak();
    cp5.addSlider("agentStepSize", 1, 30);
    minimThresholdSetter = new MinimThresholdSetter(cp5, "threshold", 256, this); // Utilisation d'un nombre standard d'échantillons pour le buffer interne : 256 (autre valeur possible : 512)
    minimThresholdSetter.setPosition(10, 144); // La méthode 'linebreak' ne fonctionne pas avec cette librairie
    minimThresholdSetter.setRange(0, 0.5);
    minimThresholdSetter.setSize(98, 98);
    minimThresholdSetter.setThresholdPercentage(0.25); // Initialisation du seuil à 25% (la valeur est modifiable avec la souris par la suite)
    minimThresholdSetter.setColorCaptionLabel(color(0));
}

void draw() 
{
    if (minimThresholdSetter.lastLevel > 0)
    {
        field.noiseScale = random(maxNoiseScale); // Sélection d'une nouvelle valeur aléatoire de noiseScale à chaque fois que le seuil est franchi
    }
    fill(255, refreshAlpha);
    noStroke();
    rect(0, 0, width, height);
    for (Agent a : agents)
    {
        a.angle = field.getNoiseValue(a.position);
        a.updatePosition();
    }
    stroke(0, agentAlpha);
    strokeWeight(agentSize);
    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }
}

void keyPressed()
{
    if (key == 'j')
    {
        save("recorded/" + getTimestamp() + ".jpg");
    }
}

void agentStepSize(float value)
{
    for (Agent a : agents)
    {
        a.stepSize = value;
    }
}