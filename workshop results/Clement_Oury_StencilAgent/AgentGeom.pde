import java.util.ArrayList;

import processing.core.PVector;
import processing.core.PApplet;
import geomerative.*;

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

