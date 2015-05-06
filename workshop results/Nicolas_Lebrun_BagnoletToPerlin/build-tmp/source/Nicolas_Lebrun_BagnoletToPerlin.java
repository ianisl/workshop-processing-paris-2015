import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 
import controlP5.*; 
import processing.opengl.*; 
import java.util.Calendar; 

import peasy.test.*; 
import peasy.org.apache.commons.math.*; 
import peasy.*; 
import controlP5.*; 
import peasy.org.apache.commons.math.geometry.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Nicolas_Lebrun_BagnoletToPerlin extends PApplet {





// Param\u00e8tres
int agentCount = 1;
float fieldIntensity = 0.02f;
float blurLevel = 0;
float boxWeight = 5;
float lineAlpha = 255;
float stepSize = 1;

float refreshAlpha = 78;
int rotationPeriodicity = 1;
float cameraRotationIntensity = 1;
int axisLength = 0;
int ligthStrenght = 540;


// Variables
ImageField field;
ArrayList<Agent> agents;
ArrayList<PVector> points;
PeasyCam cam;
ControlP5 cp5;

// Ressources
PImage background;
PImage texture;

public void setup() 
{
    size(800, 800, P3D);
    background = loadImage("carte-ign-bagnolet-resize.jpg");
    texture = loadImage("rszd-texture.jpg");
    
    
    field = new ImageField(fieldIntensity, "carte-ign-bagnolet-resize.jpg"); 
    field.blur(blurLevel);
    
    agents = new ArrayList<Agent>();
    points = new ArrayList<PVector>();
    
    
    for(int x = 0; x < 30; x ++){
          for(int y = 0; y < 30; y ++){
            
            Agent a = new Agent(new PVector( x, 0));
            agents.add(a);
          }
    }
    
    cam = new PeasyCam(this, 400);
    cam.setRotations(0, PI / 2, PI / cameraRotationIntensity);
    
    //remove peasy.cam mouse action
    cam.setActive(false);
    
    cp5 = new ControlP5(this);
    cp5.setColorCaptionLabel(color(255));
    cp5.addSlider("refreshAlpha", 1, 255).linebreak();
    cp5.addSlider("lineAlpha", 1, 255).linebreak();
    cp5.addSlider("boxWeight", 1, 20).linebreak();
    cp5.addSlider("fieldIntensity", 0, 0.1f).plugTo(field).linebreak();
    cp5.addSlider("rotationPeriodicity", 1, 600).linebreak();
    cp5.addSlider("ligthStrenght", 1, 1000).linebreak();
    cp5.setAutoDraw(false);

}

public void draw() 
{
  lights();
  pointLight(ligthStrenght, ligthStrenght, ligthStrenght, 400, 400, 400);

  fill(refreshAlpha);
  noStroke();
  box(width * 4 , height * 4, 1);
  
  if(frameCount % rotationPeriodicity == 0)
  {

    cameraRotationIntensity += PI / 180;
    cam.setRotations(- PI / 4,  0, 0);

  }

  for (Agent a : agents)
  {
      a.angle = field.getBrightness(a.position); 
      a.updatePosition();
      points.add(a.position.get());
  }

  rotateZ(cameraRotationIntensity);
  pushMatrix();
  translate( -width/2, -height/2, 10 );
  image(background, 0, 0);
  textureWrap(REPEAT); 

  for (Agent a : agents)
  {  
    float z = (field.getBrightness(a.position)/255) * 800;

    pushMatrix();
    translate(a.previousPosition.x, a.previousPosition.y, z/2);
    beginShape();
    texture(texture);
    vertex(0, 0, 0, 0, 0);
    vertex(0, 0, z*boxWeight, 0, z*boxWeight);
    vertex(boxWeight, 0, z*boxWeight, boxWeight/2, z*boxWeight);
    vertex(boxWeight, 0, 0, 0);
    endShape(CLOSE);

    beginShape();
    texture(texture);
    vertex(boxWeight, 0, 0, 0, 0);
    vertex(boxWeight, 0, z*boxWeight, 0, z*boxWeight);
    vertex(boxWeight, boxWeight / -1, z*boxWeight, boxWeight, z*boxWeight);
    vertex(boxWeight, boxWeight / -1, 0, 0, 0);
    endShape(CLOSE);

    beginShape();
    texture(texture);
    vertex(boxWeight , boxWeight / -1, 0, 0, 0);
    vertex(boxWeight , boxWeight / -1, z*boxWeight, 0, z*boxWeight);
    vertex(0, boxWeight / -1, z*boxWeight, boxWeight, z*boxWeight);
    vertex(0, boxWeight / -1, 0, 0, 0);
    endShape(CLOSE);

    beginShape();
    texture(texture);
    vertex(0, boxWeight / -1, 0, 0, 0);
    vertex(0, boxWeight / -1, z*boxWeight, 0, z*boxWeight);
    vertex(0, 0, z*boxWeight, boxWeight, z*boxWeight);
    vertex(0, 0, 0, 0, 0);
    endShape(CLOSE);

    beginShape();
    texture(texture);
    vertex(0, 0, z*boxWeight, 0, 0);
    vertex(boxWeight, 0, z*boxWeight, boxWeight, 0);
    vertex(boxWeight, boxWeight / -1, z*boxWeight, 0, z*boxWeight);
    vertex(0, boxWeight / -1, z*boxWeight, 0, 0);
    endShape(CLOSE);
    
    popMatrix();
  }
  popMatrix();
  
  gui();

}

public void gui() {
  hint(DISABLE_DEPTH_TEST);
  cam.beginHUD();
  pushMatrix();
  fill(150,150,150);
  rect(0, 0, 200, 170);
  popMatrix();
  cp5.draw();
  cam.endHUD();
  hint(ENABLE_DEPTH_TEST);
}

public void draw3DAxis(int l)
{
  stroke(255, 0, 0);
  line( width/2, height/2, 10, width/2 + l, height/2, 10);
  
  stroke(0, 0, 255);
  line( width/2, height/2, 10, width/2, height/2 + l, 10);
  
  stroke(0, 255, 0);
  line( width/2, height/2, 10, width/2, height/2, 10 + l);
  
  stroke(255, lineAlpha);
  strokeWeight(boxWeight);
  
  noStroke();
}

public void keyPressed()
{
    if (key == 'j')
    {
        save("recorded/" + getTimestamp() + ".jpg");
    }

}
class Agent 
{
    PVector position; 
    PVector previousPosition;
    float stepSize;
    float angle;    
     
    Agent() 
    {
        refreshAgent();
        stepSize = 1;
    }

    Agent(PVector position)
    {
        this();
        //this.position = position;  
        previousPosition = position.get();
    }
    public void refreshAgent()
    {
      position = new PVector(random(10, width-10), random(10, height-10));
      previousPosition = position.get();
      angle = random(2 * PI);
    } 
    public void updatePosition() 
    {
        
        int whereAgentIsGone = isOutsideSketch();

        if (whereAgentIsGone > 0 ) 
        {
            //position = new PVector(random(width), random(height));         
            
            //float newAngle = angle;
            //println(whereAgentIsGone);

            switch (whereAgentIsGone){
              case(1):
                //newAngle = -angle;
                position.y = height;
                break;  
              
              case(2):
                //newAngle = PI - angle;
                position.x =  0;
                break;
                
              case(3):
                //newAngle = -angle;
                position.y = 0;
                break;
               
              case(4):
                //newAngle = PI - angle;
                position.x =  width;
                break;
                
              default:
                //println("No agent outside");
                break;
            }
          //angle = newAngle;
        }
        previousPosition = position.get();
        position.x += cos(angle) * stepSize;
        position.y += sin(angle) * stepSize;

    }
    
    public int isOutsideSketch()
    {
        if (position.y < 0 ) 
        {
            return 1;
        } 
        else if (position.x > width ) 
        {
            return 2;
        } 
        else if (position.y > height )
        {
            return 3;
        }
        else if (position.x < 0 )
        {
            return 4;
        } 
        else
        {
            return 0;
        }
    }
}
class ImageField
{
    float fieldIntensity; // Intensit\u00e9 du champ de force
    PImage image; // Fichier image (g\u00e9om\u00e9trie du champ de force)

    // Le constructeur permet de charger une image \u00e0 partir du chemin d'acc\u00e8s
    // au fichier.
    ImageField(float fieldIntensity, String imagePath) 
    {
        this.fieldIntensity = fieldIntensity;
        this.image = loadImage(imagePath);
    }

    // M\u00e9thode permettant d'obtenir la luminosit\u00e9 de l'image \u00e0 une position donn\u00e9e
    public float getBrightness(PVector position)
    {
        int c = getColor(position);
        return brightness(c) * fieldIntensity;
    }

    // Il peut \u00eatre utile de d\u00e9finir \u00e9galement une m\u00e9thode permettant d'obtenir
    // la couleur de l'image \u00e0 une position donn\u00e9e.
    public int getColor(PVector position)
    {
        return image.get(PApplet.parseInt(position.x/width * image.width), PApplet.parseInt(position.y/height * image.height));
    }

    // M\u00e9thode permettant d'appliquer un flou \u00e0 l'image
    public void blur(float blurLevel)
    {
        image.filter(BLUR, blurLevel);
    }
}


// Cette fonction g\u00e9n\u00e8re un timestamp (information sur la date et l'heure d'ex\u00e9cution)
// sous la forme 2015-04-05_20h-30m-22s
public String getTimestamp() 
{
    Calendar now = Calendar.getInstance();
    return String.format("20%1$ty-%1$tm-%1$td_%1$tHh%1$tMm%1$tSs", now);
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Nicolas_Lebrun_BagnoletToPerlin" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
