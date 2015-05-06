import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.Frame; 
import java.util.ArrayList; 
import controlP5.ControlP5; 
import processing.core.PApplet; 
import processing.core.PVector; 
import geomerative.*; 
import java.util.ArrayList; 
import processing.core.PVector; 
import processing.core.PApplet; 
import geomerative.*; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Comparator; 
import processing.core.PApplet; 
import processing.core.PVector; 
import java.util.Calendar; 

import geomerative.*; 
import controlP5.*; 
import org.apache.batik.svggen.font.table.*; 
import org.apache.batik.svggen.font.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Clement_Oury_StencilAgent extends PApplet {

/*
 *    StencilAgent
 *      R\u00e9alis\u00e9 pendant le Workshop Processing Paris 2015
 *      Export d'un sketch 2D en fichiers vectoriel afin de d\u00e9couper un pochoir 
 *      multi couches permettant de reproduire  le plus possible fid\u00e8lement l'image g\u00e9n\u00e9r\u00e9e.
 *      Les pochoirs sont ensuite d\u00e9coup\u00e9s \u00e0 l'aide d'un robot de d\u00e9coupe (plotter).
 *        touche 'd' pour lancer la g\u00e9n\u00e9ration
 *        touche 's' pour convertir l'image actuelle en .svg et en .jpg 
 *      Le code a \u00e9t\u00e9 r\u00e9alise sous Eclipse puis remis au format .pde
 *
 *      contact: ourycl@gmail.com 
 *      web: www.bricodeur.fr
 */ 










  // Declare the objects we are going to use, so that they are accesible from
  // setup() and from draw()
  ListAgent agentsFront;
  public PerlinNoiseField field;
  public float fieldIntensity = 5;
  public float noiseScale = 200;
  private ControlP5 cp5;
  private ControlFrame cf;
  private int agentCount = 50;
  public int agentSize = 20;
  public int stepSize = 0;
  public int LIFETIME = 10000;
  public int colorFront = 100;
  public float limit = 500;
  public boolean isInspectLayer = false;
  public ArrayList<RShape> layers;
  private int showLayerTime;
  private int numLayer;
  private boolean isRecording = false;

  public void setup() {
    // Initialize the sketch
    size(600, 600);
    frameRate = 10;
    cf = addControlFrame("extra", 200, 200);
    RG.init(this);
    field = new PerlinNoiseField(this, fieldIntensity, noiseScale);
    background(255);
    agentsFront = new ListAgent();
    initListAgents(agentsFront);
    layers = new ArrayList<RShape>();
  }

  private void initListAgents(ArrayList<AgentGeom> list) {
    for (int i = 0; i < agentCount; i++) {
      AgentGeom a = new AgentGeom(this);
      a.angle = random(0, 2 * PI);
      a.position.z = i;
      list.add(a);
    }
  }

  public void draw() {
    pushMatrix();
    background(255);
    updateListAgents(agentsFront);
    fill(colorFront, 255);
    if (!isInspectLayer) {
      noStroke();
      drawListAgents(agentsFront);
      if (isRecording) {
        endRaw();
        save("file" + getTimestamp() + ".jpg");
        isRecording = false;
      }
    } else {
      showLayerTime++;      
      drawLayers();
    }
    popMatrix();
  }

  private void drawListAgents(ArrayList<AgentGeom> list) {
    // dessine le centre
    for (AgentGeom a : list) {
      pushMatrix();
      beginShape(QUAD);
      vertex(a.position.x + agentSize, a.position.y + agentSize);
      vertex(a.position.x - agentSize, a.position.y + agentSize);
      vertex(a.position.x - agentSize, a.position.y - agentSize);
      vertex(a.position.x + agentSize, a.position.y - agentSize);
      endShape();
      popMatrix();
    }
  }
  
  public void drawLayers() {
      noStroke();      
      numLayer = layers.size() - 1;
      for (int i = 0; i <= numLayer; i++) {
        layers.get(i).draw();
      }    
  }

  private void updateListAgents(ArrayList<AgentGeom> list) {
    for (AgentGeom a : list) {
      if (a.life % a.lifetime == 0) {
        a.angle += field.getNoiseValue(a.position);
      }
      a.life += 1;
      a.updatePosition();
    }
  }

  public int isOutsideSketch(PVector position) {
    if (position.y < -limit)
      return 1;
    else if (position.x > limit)
      return 2;
    else if (position.y > limit)
      return 3;
    else if (position.x < -limit)
      return 4;
    else
      return 0;
  }

  public void keyPressed() {
    if (key == 's') {
      String strTime = getTimestamp();
      saveListAGentToSvg("export/"+strTime, agentsFront);
      for (AgentGeom a : agentsFront) {
        a.stepSize = 0;
      }
      isInspectLayer = true;
      drawLayers();
      save("export/"+strTime +".jpg");      
    } else if (key == 'd') {
      for (AgentGeom a : agentsFront) {
        a.stepSize = 1;
      }
      isInspectLayer = false;
    } else if (key == 'r') {
      isRecording = true;
    }
  }

  private void saveListAGentToSvg(String filename, ArrayList<AgentGeom> list) {
    RShape shape = new RShape();
    RGroup grp = new RGroup();
    RSVG svg = new RSVG();
    float x, y;

    layers.clear();
    layers.add(new RShape());
    for (AgentGeom a : list) {
      RPolygon p = new RPolygon();
      pushMatrix();

      x = a.position.x + agentSize;
      y = a.position.y + agentSize;
      p.addPoint(x, y);

      x = a.position.x - agentSize;
      y = a.position.y + agentSize;
      p.addPoint(x, y);

      x = a.position.x - agentSize;
      y = a.position.y - +agentSize;
      p.addPoint(x, y);

      x = a.position.x + agentSize;
      y = a.position.y - agentSize;
      p.addPoint(x, y);

      popMatrix();
      RShape agentShape = p.toShape();
      addShapeToLayers(layers, agentShape);
    }

    for (RShape layer : layers) {
      svg.saveShape(filename + layers.indexOf(layer) +".svg", layer);
    }
  }

  public void addShapeToLayers(ArrayList<RShape> layers, RShape shape) {
    boolean isAdded = false;
    RShape layer;
    int i = 0;
    if (layers.size() == 0) {
      RShape newLayer = new RShape();
      newLayer.addShape(shape);
      layers.add(newLayer);
    }
    while ((i < layers.size()) && !isAdded) {
      layer = layers.get(i);
      if (!shape.intersects(layer)) {
        layer.addShape(shape);
        isAdded = true;
        break;
      }
      i++;
    }

    if (!isAdded) {
      RShape newLayer = new RShape();
      newLayer.addShape(shape);
      newLayer.setFill(color(random(200)));
      newLayer.setAlpha(255);
      layers.add(newLayer);
    }

    // couleur
    for (int j = 0; j < layers.size(); j++) {
      layers.get(j).setAlpha(255);
      layers.get(j).setFill(color(map(j, 0, layers.size(), 200, 50)));
    }
  }

  public ControlFrame addControlFrame(String theName, int theWidth, int theHeight) {
    Frame f = new Frame(theName);
    ControlFrame p = new ControlFrame(this, theWidth, theHeight);
    f.add(p);
    p.init();
    f.setTitle(theName);
    f.setSize(p.w, p.h);
    f.setLocation(800, 100);
    f.setResizable(false);
    f.setVisible(true);
    return p;
  }

  public class ControlFrame extends PApplet {

    int w, h;
    int abc = 100;
    ControlP5 cp5;
    PApplet parent;

    public void setup() {
      size(w, h);
      frameRate(25);
      cp5 = new ControlP5(this);
      cp5.addSlider("stepSize").setRange(0, 50).plugTo(stepSize).setValue(stepSize).linebreak();
      cp5.addSlider("fieldIntensity").setRange(1, 1000).plugTo(field.fieldIntensity)
          .setValue(field.fieldIntensity).linebreak();
      cp5.addSlider("noiseScale").setRange(1, 1000).plugTo(field.noiseScale)
          .setValue(field.noiseScale).linebreak();

    }

    public void draw() {
      background(abc);
    }

    private ControlFrame() {
    }

    public ControlFrame(PApplet theParent, int theWidth, int theHeight) {
      parent = theParent;
      w = theWidth;
      h = theHeight;
    }

    public ControlP5 control() {
      return cp5;
    }

    public void stepSize(float value) {
      for (AgentGeom a : agentsFront) {
        a.stepSize = round(value);
      }
    }

  }








public class AgentGeom {
  public float stepSize = 0f;
  public int agentSize = 1;

  PApplet p;
  public float angle;
  boolean isPositionResetWhenOutside;
  RPolygon poly;
  PVector position, previousPosition;  
  public PerlinNoiseField noiseField;
  public int life = 0;
  public int lifetime;
  public float maxLife = 200;
  public float worldSize;

  public AgentGeom(PApplet parent) {
    p = parent;
    // calcul les points
    randomizeCenterPosition();
    previousPosition = position.get();
    isPositionResetWhenOutside = true;
  }

  public void randomizeCenterPosition() {
    life = 0;
    position = new PVector();
    position.x = p.map(p.random(100), 0, 100, agentSize*2, p.width-agentSize*2); 
    position.y = p.map(p.random(100), 0, 100, agentSize*2, p.height-agentSize*2);
    
    lifetime = (int) (p.random(maxLife)+(maxLife/2));
    poly = new RPolygon();
    poly.addPoint(p.round(position.x + p.random(agentSize)), p.round(position.y + p.random(agentSize)));
    poly.addPoint(p.round(position.x - p.random(agentSize)), p.round(position.y + p.random(agentSize)));
    poly.addPoint(p.round(position.x - p.random(agentSize)), p.round(position.y - p.random(agentSize)));
    poly.addPoint(p.round(position.x + p.random(agentSize)), p.round(position.y - p.random(agentSize)));
    angle = p.random(0, 2 * p.PI);
  }

  public void updatePosition() {
    RPoint[] points = poly.getPoints();
    previousPosition = position.get();
    
    p.pushMatrix();  
    position.x += stepSize * p.cos(angle);
    position.y += stepSize * p.sin(angle);
    p.popMatrix();
    
    PVector offset = PVector.sub(position, previousPosition);    
//     on fait evoluer la forme
    for(int i=0; i < points.length; i ++) {
     points[i].x += offset.x;
     points[i].y += offset.y;
    }
  }

}





public class ListAgent extends ArrayList<AgentGeom> {
  
  ListAgent() {
  }
  
  public void orderByZOrder() {
    Collections.sort(this, new CustomComparator());
  }
  
  public class CustomComparator implements Comparator<AgentGeom> {
    public int compare(AgentGeom arg0, AgentGeom arg1) {      
      return (int) (arg1.position.z-arg0.position.z);
    }
  }

}




public class PerlinNoiseField {
  PApplet p;
  public float fieldIntensity; //intensit\u00c3\u00a9
  public float noiseScale; // g\u00c3\u00a9om\u00c3\u00a9trie
  
  public PerlinNoiseField(PApplet parent, float fieldIntensity, float noiseScale) {
    this.p = parent;
    this.fieldIntensity = fieldIntensity;
    this.noiseScale = noiseScale; 
  }
  
  public float getNoiseValue(PVector position) {
    return p.noise(position.x / noiseScale, position.y / noiseScale)*fieldIntensity;
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
        String[] appletArgs = new String[] { "Clement_Oury_StencilAgent" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
