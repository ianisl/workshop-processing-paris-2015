import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Camille_Boulouis_JSON extends PApplet {

//Parameters
int agentCount = 2;
float agentSize = 10;
float stepSize = 1;
int sketchSize = 800;
float fieldIntensity = 10;
float noiseScale = 300;
float agentAlpha = 10;

//Variables
PerlinNoiseField field;
ArrayList<Agent> agents;


public void setup() 
{  
  size (sketchSize, sketchSize);
  smooth();
  background(255);
  field = new PerlinNoiseField(fieldIntensity, noiseScale);
  agents = new ArrayList<Agent>();

  JSONObject json = loadJSONObject("LocationHistory2.json");
  JSONArray locations = json.getJSONArray("locations"); 

  for (int i = 0; i < locations.size (); i++) {

    JSONObject c = locations.getJSONObject(i);
    float lat = c.getFloat("latitudeE7");
    float lon = c.getFloat("longitudeE7");
    JSONArray activities = c.getJSONArray("activitys").getJSONObject(0).getJSONArray("activities");
    String activityType = activities.getJSONObject(0).getString("type");
    println("lat " + lat + " lon " + lon + " type " + activityType); 

    float x = lngToXWorld(lon, sketchSize);
    float y = latToYWorld(lat, sketchSize);

    Agent a = new Agent(new PVector(x, y));
    agents.add(a);

    if (activityType.equals("still"))
    {
      a.angle = random(2*PI);
      a.stepSize = stepSize;
      a.agentSize = agentSize;
    }
    if (activityType.equals("onFoot"))
    {
      a.angle = random(2*PI);
      a.stepSize = stepSize * 4;
      a.agentSize = agentSize * 3;
    }
    if (activityType.equals("inVehicle"))
    {
      a.angle = random(2*PI);
      a.stepSize = stepSize * 10;
    }
     if (activityType.equals("onBicycle"))
    {
      a.angle = random(2*PI);
      a.stepSize = stepSize * 8;
    }
  }



  //  for (TableRow row : table.rows()) 
  //  {
  //
  //    float Xlon = row.getFloat("Xlon");
  //    float Ylat = row.getFloat("Ylat");
  //    float x = lngToXWorld(Xlon, sketchSize);
  //    float y = latToYWorld(Ylat, sketchSize);
  //    println("longitude : "+ x + " " + "latitude : "+y);
  //    Agent a = new Agent(new PVector(x, y));
  //    agents.add(a);
  //
  //  }
}

public void draw() 
{
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit \u00e0 la position de l'agent comme nouvelle valeur de l'angle
    a.updatePosition();
  }    

  noFill();
  stroke(255, 0, 0, agentAlpha);

  for (Agent a : agents)
  {
    strokeWeight(a.agentSize);
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
}

class Agent 
{
    // Attributs de la classe
    PVector position; // Position de l'agent
    PVector previousPosition; // Stockage de la position pr\u00e9c\u00e9dente (pour le dessin)
    float stepSize; // Incr\u00e9ment de d\u00e9placement (= vitesse de base de l'agent)
    float angle; // Angle de d\u00e9placement de l'agent
    float agentSize;
    boolean isPositionResetWhenOutside; // Permet d'activer ou non la r\u00e9initialisation de la position de l'agent lorsqu'il quitte l'espace du sketch

    // Le constucteur par d\u00e9faut de la classe
    Agent() 
    {
      
        position = new PVector(random(width), random(height)); // Position al\u00e9atoire
        previousPosition = position.get(); // Attention \u00e0 bien copier le PVector avec la m\u00e9thode 'get()';
        angle = random(2 * PI); // Angle al\u00e9atoire
        stepSize = 1;
        isPositionResetWhenOutside = true;
    }

    // Un autre constructeur dont l'argument 'position' permet de sp\u00e9cifier une position
    Agent(PVector position)
    {
        this(); // Appel du constructeur par d\u00e9faut pour initialiser tous les attributs
        this.position = position; // Mise \u00e0 jour de l'attribut position avec l'argument 'position' pass\u00e9 au constructeur. Comme l'argument du constructeur et l'attribut ont le m\u00eame nom, on identifie l'attribut en le faisant pr\u00e9c\u00e9der de 'this.'
        previousPosition = position.get();
    }

    // Une m\u00e9thode de la classe permettant de mettre \u00e0 jour la position de l'agent (en fonction de son angle de d\u00e9placement actuel)
    public void updatePosition() 
    {
        previousPosition = position.get(); // Sauvegarde de la position pr\u00e9c\u00e9dente
        position.x += cos(angle) * stepSize; // L'agent avance sur une distance \u00e9gale \u00e0 'stepSize' \u00e0 partir de sa position actuelle, selon un angle 'angle'
        position.y += sin(angle) * stepSize;
        if (isOutsideSketch() > 0 && isPositionResetWhenOutside) 
        {
            position = new PVector(random(width), random(height)); // Si l'agent sort du sketch, on lui attribue une nouvelle position al\u00e9atoire
            previousPosition = position.get();
        }
    }
    
    // Une m\u00e9thode permettant de v\u00e9rifier si l'agent est sorti des limites de l'espace du sketch (+ marges)
    // La m\u00e9thode renvoie les valeurs suivantes :
    // 0: l'agent n'est pas sorti des limites de l'espace du sketch
    // 1: l'agent est sorti par le haut
    // 2: l'agent est sorti par la droite
    // 3: l'agent est sorti par le bas
    // 4: l'agent est sorti par la gauche
    public int isOutsideSketch()
    {
        if (position.y < 0) 
        {
            return 1;
        } 
        else if (position.x > width) 
        {
            return 2;
        } 
        else if (position.y > height)
        {
            return 3;
        }
        else if (position.x < 0)
        {
            return 4;
        } 
        else
        {
            return 0;
        }
    }
}
public float lngToXWorld(float lon, float projectionSize)
{
    float radius = projectionSize / (2 * PI);
    float falseEasting = -1.0f * projectionSize / 2.0f;
    return (radius * lon * PI / 180) - falseEasting;
  }

// Convert latitude to y world coordinate
public float latToYWorld(float lat, float projectionSize) 
{
    float radius = projectionSize / (2 * PI);
    float falseNorthing = projectionSize / 2;
    return ((radius / 2 * log((1 + sin(lat * PI / 180)) / (1 - sin(lat * PI / 180)))) - falseNorthing) * (-1);
}
class PerlinNoiseField
{
    float fieldIntensity; // Intensit\u00e9 du champ de force
    float noiseScale; // 'G\u00e9om\u00e9trie' du bruit

    PerlinNoiseField(float fieldIntensity, float noiseScale) 
    {
        this.fieldIntensity = fieldIntensity;
        this.noiseScale = noiseScale;
    }

    // M\u00e9thode permettant d'obtenir la valeur du champ de force \u00e0 une position donn\u00e9e
    public float getNoiseValue(PVector position)
    {
        return noise(position.x / noiseScale, position.y / noiseScale) * fieldIntensity;
    }
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Camille_Boulouis_JSON" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
