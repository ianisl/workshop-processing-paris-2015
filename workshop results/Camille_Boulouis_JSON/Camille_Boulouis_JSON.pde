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

void setup() 
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

}

void draw() 
{
  for (Agent a : agents)
  {
    a.angle = field.getNoiseValue(a.position); // Utilisation de la valeur du bruit à la position de l'agent comme nouvelle valeur de l'angle
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