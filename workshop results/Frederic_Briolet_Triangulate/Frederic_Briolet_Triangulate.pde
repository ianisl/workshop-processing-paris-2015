import org.processing.wiki.triangulate.*;

// Paramètres
int agentCount = 1000;
float fieldIntensity = 0.03;
float blurLevel = 1;
float edgeWeight = 1;
float edgeAlpha = 1;
float circleAlpha = 2;
float circleColorHue = 350; 
float circleColorSaturation = 125;
float circleColorBrightness = 100;
float brightnessStep = 0.2; // Vitesse du dégradé au cours du temps. L'espace HSB permet de générer des variations autour d'une teinte de base (ici, le bleu).
float saturationStep = -0.1;

// Variables
ImageField field;
ArrayList<Agent> agents;
ArrayList<PVector> points; // La librairie 'triangulate' travaille sur un ArrayList de 'PVector'
ArrayList<Triangle> triangles; // La librairie renvoie ses résultats sous forme d'une liste de triangles

void setup() 
{
    size(1000, 1000);
    smooth(8);
    background(0);
    colorMode(HSB, 360, 100, 100);
    field = new ImageField(fieldIntensity, "onionskin.jpg");
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
        points.add(a.position);
    }
    triangles = Triangulate.triangulate(points); // Utilisation de la méthode 'triangulate' contenue dans la classe 'Triangulate'. On ne crée pas d'objet : on dit que la méthode est 'statique'.
    strokeWeight(edgeWeight);
    noFill();
    beginShape(TRIANGLES); // Il est ici pratique d'utiliser ce mode de dessin pour tracer des triangles
    stroke(255, edgeAlpha);
    for (Triangle t : triangles)
    {
        stroke(color(circleColorHue, circleColorSaturation, circleColorBrightness), circleAlpha);
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
