import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Clement_Oury_Circle extends PApplet {



  // PARAMS
  int NUM_VERTEX = 100;
  float AGENT_SIZE = 1;
  float RADIUS = 100;
  int agentStepSize = 1;
  float fieldIntensity = 200;  
  float noiseScale = 1;
  float agentAlpha = 5;
  int stopFrame = 110;
  int numFrame = 300;
  
  // VARIABLES
  ArrayList<Agent> list;
  public PerlinNoiseField field;
  ControlP5 cp5;
  
  public void setup() {
    cp5 = new ControlP5(this);
    frameRate(20);
    noFill();    
    size(500, 600);    
    background(255);
    initAgent();
    field = new PerlinNoiseField(this, fieldIntensity, noiseScale);
    
    cp5.setColorCaptionLabel(color(0));
    cp5.addSlider("NUM_VERTEX").setRange(1, 500)
    .setLabel("num vertex")
    .setValue(NUM_VERTEX)
    .linebreak();
    cp5.addSlider("fieldIntensity").setRange(1, 1000)
    .plugTo(field.fieldIntensity)
    .setValue(fieldIntensity)
    .linebreak();    
    cp5.addSlider("noiseScale").setRange(1, 500)
    .plugTo("field")
    .setValue(field.noiseScale)
    .linebreak(); 
    cp5.addSlider("agentAlpha").setRange(1, 50)
    .setValue(agentAlpha)
    .linebreak();
    cp5.addSlider("numFrame").setRange(150, 1000)
    .setValue(numFrame)
    .linebreak();    
  }

  public void initAgent() {
    list = new ArrayList<Agent>();    
    for (int i = 0; i < NUM_VERTEX; i++) {
      Agent a = new Agent(this);
      a.position.x = cos((2*PI)*i/NUM_VERTEX)*RADIUS+width/2;
      a.position.y = sin((2*PI)*i/NUM_VERTEX)*RADIUS+height/2;
      a.isPositionResetWhenOutside = false;
      list.add(a);
    }
  }
  
  public void draw() {
    stroke(20, agentAlpha);
    if ((frameCount % numFrame) == 0) {
      list = new ArrayList<Agent>();
      initAgent();
      background(255);
      
    } else if((frameCount % numFrame) < (numFrame-stopFrame) ) { 
    beginShape();
      for (Agent a : list) {
        field.noiseScale = sin(a.position.x)*100;
        field.fieldIntensity = cos(a.position.y)*100;
        a.angle = field.getNoiseValue(a.position);
        a.updatePosition();
        //curveVertex():
        curveVertex(a.previousPosition.x, a.previousPosition.y);
      }
      endShape(CLOSE);
    } else
    {
      fill(255, 30);
      rect(0, 0, width, height);
      noFill();
    }
  }

  class Agent {
    PVector position;
    PVector previousPosition;
    int stepSize = 2;
    float angle;
    boolean isPositionResetWhenOutside;
    PApplet p;

    Agent(PApplet parent) {
      p = parent;
      position = new PVector(parent.random(p.width), p.random(p.height));
      previousPosition = position.get();
      angle = p.random(0, 2 * p.PI);
      isPositionResetWhenOutside = true;
    }
    
    Agent(PApplet parent, PVector position) {
      this.position = position;
    }

    public void updatePosition() {
      // agent se d\u00e9place en ligne droite par une distance d\u00e9fini par
      // stepSize
      previousPosition = position.get();
      position.x += stepSize * p.cos(angle);
      position.y += stepSize * p.sin(angle);
      position.z += stepSize * p.sin(angle);
      if (isPositionResetWhenOutside && isOutsideSketch() != 0) {
        position = new PVector(p.random(p.width), p.random(p.height));
        previousPosition = position.get();
      }
    }
    
    public int isOutsideSketch() {
      if (position.y < 0)
        return 1;
      else if (position.x > p.width)
        return 2;
      else if (position.y > p.height)
        return 3;
      else if (position.x < 0)
        return 4;
      else
        return 0;
    }
  }
  
  class PerlinNoiseField {
    PApplet p;
    public float fieldIntensity; //intensit\u00e9
    public float noiseScale; // g\u00e9om\u00e9trie
    
    public PerlinNoiseField(PApplet parent, float fieldIntensity, float noiseScale) {
      this.p = parent;
      this.fieldIntensity = fieldIntensity;
      this.noiseScale = noiseScale; 
    }
    
    public float getNoiseValue(PVector position) {
      return p.noise(position.x / noiseScale, position.y / noiseScale)*fieldIntensity;
    }
  }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Clement_Oury_Circle" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
