import org.processing.wiki.triangulate.*;

int agentCount = 80;
int pointCount = 100;
float stepSize = 2;
float floor=100;
float coeff = 0.0007;
float refreshAlpha=22;
int refreshFramePeriodicity = 10 ;
int refreshVisage = 300;
int margin = 50;
boolean expansion = true ;
int compteur=int(random(26));

PImage visage ;
color colorTriangle;
float sqDistanceBetweenPointAndAgent;
ArrayList<PVector> points ;
ArrayList<Agent> agents;
ArrayList<Triangle> triangles;
HashMap<Triangle, Integer> triangleColors;
float floorSqr;
float leftLimit, rightLimit, topLimit, bottomLimit ;

void setup() 
{ 
  size(800, 800);

  background(255);
  rectMode(CENTER);
  smooth();

  resizeimage();

  points = new ArrayList<PVector>();
  agents = new ArrayList<Agent>();
  triangles = new ArrayList<Triangle>();
  triangleColors = new HashMap<Triangle, Integer>();


  for (int i = 0; i < pointCount; i++)
  {
    PVector p = new PVector(random(leftLimit, visage.width+rightLimit), random(topLimit, visage.height+bottomLimit)); 
    points.add(p);
  }

  for (int i = 0; i < agentCount; i++)
  { 
    Agent a = new Agent();
    a.stepSize = stepSize;
    agents.add(a);
  }

  floorSqr = floor * floor;
  triangles = Triangulate.triangulate(points);
  points.clear();

  for (Triangle t : triangles)
  {
    points.add(t.p1);
    points.add(t.p2);
    points.add(t.p3);
    triangleColors.put(t, visage.get(int(t.p1.x/width * visage.width), int(t.p1.y/height * visage.height)));
  }
}

void draw() 
{
  //coeff=getInputPinValue("coeff");
  //floor=getInputPinValue("floor");

  float flipCoin=random(frameCount);
  if (flipCoin <frameCount/100) 
  { 
    expansion=!expansion;
  }

  if (frameCount % refreshVisage == 0)
  {
    refreshVisage=int(random(60, 700));
    changeimage();
  }

  if (frameCount % refreshFramePeriodicity == 0)
  {
    fill(255, refreshAlpha);
    noStroke();
    rect(width/2, height/2, width, height);
  }

  for (Agent a : agents)
  { 
    for (PVector p : points) 
    {
      a.angle=random(2*PI);
      a.updatePosition();
      sqDistanceBetweenPointAndAgent = sq(a.position.x-p.x) + sq(a.position.y-p.y);

      if ( sqDistanceBetweenPointAndAgent < floorSqr)
      {
        if (expansion) 
        {
          p.x = p.x + coeff * (a.position.x - p.x);
          p.y = p.y + coeff * (a.position.y - p.y);
        } else 
        {
          p.x = p.x - coeff * (a.position.x - p.x);
          p.y = p.y - coeff * (a.position.y - p.y);
        }
      }
    }
  }
  beginShape(TRIANGLES);
  for (int i =0; i < triangles.size (); i++)
  {
    Triangle t = triangles.get(i);
    colorTriangle = triangleColors.get(t);

    stroke(colorTriangle);
    fill(colorTriangle);
    vertex(t.p1.x, t.p1.y);
    vertex(t.p2.x, t.p2.y);
    vertex(t.p3.x, t.p3.y);
  }
  endShape();
}

void changeimage()
{
  //save("recorded/" + getTimestamp() + ".jpg");
  compteur=int(random(26));

  background(255);

  resizeimage();

  points.clear();
  triangles.clear();
  triangleColors.clear();

  triangleColors = new HashMap<Triangle, Integer>();

  for (int i = 0; i < pointCount; i++)
  {
    PVector p = new PVector(random(leftLimit, visage.width+rightLimit), random(topLimit, visage.height+bottomLimit)); 
    points.add(p);
  }


  triangles = Triangulate.triangulate(points);
  points.clear();

  for (Triangle t : triangles)
  {
    points.add(t.p1);
    points.add(t.p2);
    points.add(t.p3);
    triangleColors.put(t, visage.get(int(t.p1.x/width * visage.width), int(t.p1.y/height * visage.height)));
  }
}

void resizeimage() {

  visage=loadImage("image"+compteur+".jpg");

  if (visage.width > visage.height)

  { 
    visage.resize(width-(margin*2), 0);

    float customargin=(height-visage.height)/2;

    image(visage, margin, customargin);

    leftLimit = margin +random(visage.width/4);
    rightLimit = margin-random(visage.width/4);
    topLimit = customargin + random(visage.height/4);
    bottomLimit = customargin - random(visage.height/4);
  } else 
  {   
    visage.resize(0, height-(margin*2));

    float customargin = (width-visage.width)/2;
    image(visage, customargin, margin);

    leftLimit = customargin +random(visage.width/4);
    rightLimit = customargin-random(visage.width/4);
    topLimit = margin + random(visage.height/4);
    bottomLimit = margin - random(visage.height/4);
  }
}

