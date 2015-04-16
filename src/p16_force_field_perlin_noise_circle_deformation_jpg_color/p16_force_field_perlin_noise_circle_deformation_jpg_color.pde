// Paramètres
int agentCount = 500;
float fieldIntensity = 10;
float noiseScale = 100;
float circleRadius = 250;
float circleLineWeight = 1.5;
float circleAlpha = 20;
float circleColorHue = 220; // Définition de la couleur dans l'espace HSB (Hue Saturation Brightness).
float circleColorSaturation = 65;
float circleColorBrightness = 0;
float brightnessStep = 0.2; // Vitesse du dégradé au cours du temps. L'espace HSB permet de générer des variations autour d'une teinte de base (ici, le bleu).
float saturationStep = -0.1;

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    colorMode(HSB, 360, 100, 100); // À présent, 'color()' utilise l'espace HSB 
    field = new PerlinNoiseField(fieldIntensity, noiseScale);
    agents = new ArrayList<Agent>();
    float angleStep = 2 * PI / agentCount;
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent(new PVector(width / 2 + cos(i * angleStep) * circleRadius, height / 2 + sin(i * angleStep) * circleRadius));
        a.isPositionResetWhenOutside = false;
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
    circleColorBrightness += brightnessStep;
    circleColorSaturation += saturationStep;
    stroke(color(circleColorHue, circleColorSaturation, circleColorBrightness), circleAlpha);
    strokeWeight(circleLineWeight);
    noFill();
    beginShape();
    for (Agent a : agents)
    {
        curveVertex(a.position.x, a.position.y);
    }
    if (agents.size() > 3)
    {
        curveVertex(agents.get(0).position.x, agents.get(0).position.y);
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