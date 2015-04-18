import org.processing.wiki.triangulate.*;

// Paramètres
int agentCount = 100;
float fieldIntensity = 0.02;
float blurLevel = 15;
float edgeWeight = 1.5;
float edgeAlpha = 2;

// Variables
ImageField field;
ArrayList<Agent> agents;
ArrayList<PVector> points; // La librairie 'triangulate' travaille sur un ArrayList de 'PVector'
ArrayList<Triangle> triangles; // La librairie renvoie ses résultats sous forme d'une liste de triangles

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
}

void draw() 
{
    points.clear(); 
    for (Agent a : agents)
    {
        a.angle = field.getBrightness(a.position);
        a.updatePosition();
        points.add(a.position.get()); // Copie de la position de l'agent. Si on n'ajoute pas une copie mais seulement une référence, on permet à la librairie Triangulate de modifier la valeur des positions. Or Triangulate ramène sur le bord les points qui sortent de l'espace du sketch, ce que nous ne souhaitons pas (la forme viendrait alors se bloquer contre les bords de l'espace du sketch)
    }
    triangles = Triangulate.triangulate(points); // Utilisation de la méthode 'triangulate' contenue dans la classe 'Triangulate'. On ne crée pas d'objet : on dit que la méthode est 'statique'.
    strokeWeight(edgeWeight);
    noFill();
    beginShape(TRIANGLES); // Il est ici pratique d'utiliser ce mode de dessin pour tracer des triangles
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