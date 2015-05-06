/*
 *    StencilAgent
 *      Réalisé pendant le Workshop Processing Paris 2015
 *      Export d'un sketch 2D en fichiers vectoriel afin de découper un pochoir 
 *      multi couches permettant de reproduire  le plus possible fidèlement l'image générée.
 *      Les pochoirs sont ensuite découpés à l'aide d'un robot de découpe (plotter).
 *        touche 'd' pour lancer la génération
 *        touche 's' pour convertir l'image actuelle en .svg et en .jpg 
 *      Le code a été réalise sous Eclipse puis remis au format .pde
 *
 *      contact: ourycl@gmail.com 
 *      web: www.bricodeur.fr
 */ 

import java.awt.Frame;
import java.util.ArrayList;

import controlP5.ControlP5;

import processing.core.PApplet;
import processing.core.PVector;
import geomerative.*;

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
  
  void drawLayers() {
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

  ControlFrame addControlFrame(String theName, int theWidth, int theHeight) {
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

    void stepSize(float value) {
      for (AgentGeom a : agentsFront) {
        a.stepSize = round(value);
      }
    }

  }