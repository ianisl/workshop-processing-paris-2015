import peasy.*;
import controlP5.*;
import processing.opengl.*;

// Paramètres
int agentCount = 1;
float fieldIntensity = 0.02;
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

void setup() 
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
    cp5.addSlider("fieldIntensity", 0, 0.1).plugTo(field).linebreak();
    cp5.addSlider("rotationPeriodicity", 1, 600).linebreak();
    cp5.addSlider("ligthStrenght", 1, 1000).linebreak();
    cp5.setAutoDraw(false);

}

void draw() 
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

void gui() {
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

void draw3DAxis(int l)
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

void keyPressed()
{
    if (key == 'j')
    {
        save("recorded/" + getTimestamp() + ".jpg");
    }

}
