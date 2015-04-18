import org.processing.wiki.triangulate.*;
import controlP5.*;

// Paramètres
int agentCount = 40;
float fieldIntensity = 0.02;
float blurLevel = 15;
float edgeWeight = 1.5;
float edgeAlpha = 10;
float refreshAlpha = 20; // Si on utilise une valeur trop faible de 'refreshAlpha', le fond ne sera pas complètement effacé. Mais avec une valeur suffisament élevée pour effacer le fond, le rafraîchissement peut être trop rapide
int refreshFramePeriodicity = 50; // Solution : on peut ne rafraîchir l'écran qu'une fois toutes les 'refreshFramePeriodicity' trames d'affichage (par ex. toutes les 50 trames). Cela introduit un clignotement dans l'image mais ce ne sera pas un problème si la finalité est l'impression et non l'animation

// Variables
ImageField field;
ArrayList<Agent> agents;
ArrayList<PVector> points;
ArrayList<Triangle> triangles;
ControlP5 cp5;

void setup() 
{
    size(800, 800);
    smooth();
    background(255);
    field = new ImageField(fieldIntensity, "x.jpg");
    field.blur(blurLevel);
    agents = new ArrayList<Agent>();
    points = new ArrayList<PVector>();
    triangles = new ArrayList<Triangle>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent();
        agents.add(a);
    }
    cp5 = new ControlP5(this);
    cp5.setColorCaptionLabel(color(0));
    cp5.addSlider("agentCount", 3, 10).setValue(agentCount);
}

void draw() 
{
    if (frameCount % refreshFramePeriodicity == 0)
    {
        fill(255, refreshAlpha);
        noStroke();
        rect(0, 0, width, height);
    }
    points.clear(); 
    for (Agent a : agents)
    {
        a.angle = field.getBrightness(a.position);
        a.updatePosition();
        points.add(a.position.get());
    }
    triangles = Triangulate.triangulate(points);
    strokeWeight(edgeWeight);
    noFill();
    beginShape(TRIANGLES);
    stroke(0, edgeAlpha);
    for (Triangle t : triangles)
    {
        vertex(t.p1.x, t.p1.y);
        vertex(t.p2.x, t.p2.y);
        vertex(t.p3.x, t.p3.y);
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

void agentCount(int value)
{
    int diff = value - agents.size(); // Calcul du nombre d'agents à ajouter ou supprimer
    if (diff > 0)
    {
        for (int i = 0; i < diff; i++)
        {
            Agent a = new Agent();
            agents.add(a); // Ajout d'un nombre 'diff' de nouveaux agents
        }
    }
    else
    {
        diff = -diff; // Attention, diff est ici négatif ! On repasse à un nombre positif pour plus de lisibilité
        for (int i = 0; i < diff; i++)
        {
            agents.remove(agents.size() - 1); // Suppression d'un nombre 'diff' d'agents en partant de la fin du 'ArrayList'
        }
    }
}