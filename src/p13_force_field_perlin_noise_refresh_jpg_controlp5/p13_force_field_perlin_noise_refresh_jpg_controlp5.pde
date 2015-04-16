import controlP5.*;

// Paramètres
int agentCount = 1000;
float agentSize = 20;
float agentAlpha = 10;
float agentStepSize = 3;
float fieldIntensity = 24;
float noiseScale = 300;
float refreshAlpha = 56; // Utilisation d'un fond transparent pour créer un effet de trace partiel

// Variables
PerlinNoiseField field;
ArrayList<Agent> agents;
ControlP5 cp5;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new PerlinNoiseField(fieldIntensity, noiseScale);
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        a.stepSize = agentStepSize;
        agents.add(a);
    }
    cp5 = new ControlP5(this); // Initialisation de controlP5
    cp5.setColorCaptionLabel(color(0)); // Changement de la couleur du texte de l'interface
    cp5.addSlider("agentAlpha", 0, 255).linebreak(); // Arguments : nom de la variable à contrôler, valeur minimale, valeur maximale. La méthode 'linebreak()' permet d'ajouter l'élément d'interface suivant sur une nouvelle ligne
    cp5.addSlider("refreshAlpha", 0, 255).linebreak();
    cp5.addSlider("agentSize", 1, 50).linebreak();
    cp5.addSlider("fieldIntensity", 1, 30).plugTo(field).linebreak(); // Pour contrôler les attributs d'un objet, il faut « plugger » controlP5 dans l'objet
    cp5.addSlider("noiseScale", 1, 500).plugTo(field).linebreak();
    cp5.addSlider("agentStepSize", 1, 30).setValue(agentStepSize); // L'attribut 'stepSize' doit être modifié dans chaque objet de type Agent. Cela ne peut se faire via la méthode 'plugTo', il faut créer une fonction écoutant les changements apportés au slider. Note : il faut dans ce cas fixer la valeur initiale du slider « à la main ».
}

void draw() 
{
    fill(255, refreshAlpha);
    noStroke();
    rect(0, 0, width, height); // La fonction 'background' ne permet pas d'utiliser une couleur transparente. On la remplace donc par le tracé d'un rectangle couvrant tout l'espace du sketch
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

// Fonction écoutant les changements apportés au slider 'agentStepSize'
void agentStepSize(float value)
{
    for (Agent a : agents)
    {
        a.stepSize = value;
    }
}