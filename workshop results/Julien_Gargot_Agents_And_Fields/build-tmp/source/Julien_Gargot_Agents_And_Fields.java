import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Julien_Gargot_Agents_And_Fields extends PApplet {



/*   <This program generates a matrix of force fields like a rectangular grid. Agents walk upon it. Multiple dynamic parameters.>
 *   Copyright (C) <2015>  <Julien Gargot>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// Dynamic parameters
int    gridStepping = 2;
int    agentCount = 1;
float  agentSize = 1;
float  agentStepSize = 5;
int    refreshFieldPeriodicity = 20;
float  fieldOrientation = 0;
float  refreshAlpha = 20;
String mode = "random";

// Settings
int agentColor = color(255);
float agentAlpha = 90;
float fieldAlpha = 128;
int backgroundColor = color(0);

// Variables
ArrayList<RectangularField> fields;
ArrayList<Agent> agents;
ControlP5 cp5;
boolean displayBackground = false;
float fieldWidth;
float fieldHeight;


public void setup() {
    size(800, 800);
    smooth(8);
    fieldWidth = ((float)width) / gridStepping;
    fieldHeight = ((float)height) / gridStepping;
    fields = new ArrayList<RectangularField>();
    for (int i = 0; i < gridStepping*gridStepping; i++)
    {
        RectangularField s = new RectangularField(fieldOrientation,gridStepping,fieldAlpha);
        fields.add(s);
    }
    agents = new ArrayList<Agent>();
    for (int i = 0; i < agentCount; i++)
    {
        Agent a = new Agent(new PVector(random(width), random(height)));
        a.stepSize = agentStepSize;
        agents.add(a);
    }
    cp5 = new ControlP5(this);
    cp5.setColorCaptionLabel(color(0,109,204));
    cp5.addSlider("gridStepping",            1, 32   ).setLabel("Grid stepping").setValue(gridStepping).linebreak();
    cp5.addSlider("agentCount",              1, 2048 ).setLabel("Agent count").setValue(agentCount).linebreak();
    cp5.addSlider("agentSize",               1, 64   ).setLabel("Agent size").linebreak();
    cp5.addSlider("agentStepSize",           1, 32   ).setLabel("Agent speed").setValue(agentStepSize).linebreak();
    cp5.addSlider("refreshFieldPeriodicity", 1, 30   ).setLabel("Refresh field periodicity").linebreak();
    cp5.addSlider("fieldOrientation",        0, 2*PI ).setLabel("Force Field Orientation").setValue(fieldOrientation).linebreak();
    cp5.addSlider("refreshAlpha",            0, 255  ).linebreak();
    cp5.addBang("mode");
    cp5.addBang("displayBackground").setLabel("show background (dev)"); // Dev purpose only.
}

public void draw()
{
    // F O R M A T I N G   D A T A S
    for (Agent a : agents)
    {
        int intX = floor(a.position.x);
        int intY = floor(a.position.y);
        int intW = floor(fieldWidth);
        int intH = floor(fieldHeight);
        int j = intX / intW;
        int i = intY / intH;
        int n = i*gridStepping+j;
        if (n < fields.size()) {
            a.angle = fields.get(n).getBrightness();
        }
        a.updatePosition();
    }
    if ( frameCount % refreshFieldPeriodicity == 0)
    {
        for (int i = 0; i < gridStepping; i++) {
            for (int j = 0; j < gridStepping; j++) {
                int n = i*gridStepping+j;
                if (n < fields.size()) {
                    RectangularField s = fields.get(n);
                    if( mode == "random")  {
                        s.fieldAlpha = random(0,255);
                    }
                    else {
                        s.fieldAlpha -= random(-5,5);
                    }
                    s.position = new PVector(j*fieldWidth,i*fieldHeight);
                }

            }
        }
    }

    // D R A W I N G
    if (displayBackground) // Dev purpose only.
    {
        for (RectangularField s : fields)
        {
            s.drawMe();
        }
        stroke(0,255,255, agentAlpha);
        strokeWeight(agentSize);
    }
    else
    {
        noStroke();
        fill(backgroundColor, refreshAlpha);
        rect(0,0,width,height);
        stroke(agentColor, agentAlpha);
        strokeWeight(agentSize);
    }

    for (Agent a : agents)
    {
        line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
    }
}

public void gridStepping(int inputValue)
{
    gridStepping = inputValue;
    fieldWidth = ((float)width) / gridStepping;
    fieldHeight = ((float)height) / gridStepping;
    int n = gridStepping*gridStepping;
    int d = fields.size() - n;
    for (RectangularField s : fields)
    {
        s.updateColumnNumber(gridStepping);
    }
    if (d < 0) // add fields
    {
        for (int i = 0; i < Math.abs(d); i++)
        {
            RectangularField s = new RectangularField(fieldOrientation,gridStepping,fieldAlpha);
            fields.add(s);
        }
    }
    if (d > 0) // remove fields
    {
        for (int i = d - 1; i >= 0; i--) {
            fields.remove(i);
        }
    }
}

public void agentCount(int inputValue) {
    agentCount = inputValue;
    int d = agents.size() - agentCount;
    if (d < 0) // add agents
    {
        for (int i = 0; i < Math.abs(d); i++)
        {
            Agent a = new Agent();
            a.stepSize = agentStepSize;
            agents.add(a);
        }
    }
    if (d > 0) // remove agents
    {
        for (int i = d - 1; i >= 0; i--) {
          agents.remove(i);
        }
    }
}

public void agentStepSize(float inputValue)
{
    for (Agent a : agents)
    {
        a.stepSize = inputValue;
    }
}

public void displayBackground() { // Dev purpose only.
    displayBackground = !displayBackground;
}

public void mode() {
    mode = mode == "random" ? "breath" : "random";
}
class Agent
{
  // Attributs
  PVector position;
  PVector previousPosition;
  float   angle;
  float   stepSize;
  boolean isPositionResetWhenOutside;

  Agent()
  {
    position = new PVector(random(width), random(height));
    previousPosition = position.get();
    angle = random(2 * PI);
    stepSize = 1;
    isPositionResetWhenOutside = true;
  }

  Agent(PVector position)
  {
    this();
    this.position = position;
    previousPosition = position.get();
  }

  public void updatePosition()
  {
    previousPosition = position.get();
    position.x += cos(angle) * stepSize;
    position.y += sin(angle) * stepSize;
    if(isPositionResetWhenOutside && isOutsideStetch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }

  public int isOutsideStetch()
  {
    if (position.y < 0 ) {
      return 1;
    }
    else if (position.x > width) {
      return 2;
    }
    else if (position.y > height) {
      return 3;
    }
    else if (position.x < 0) {
      return 4;
    }
    else {
      return 0;
    }
  }
}
class RectangularField {

    float fieldOrientation;
    float fieldWidth;
    float fieldHeight;
    float fieldAlpha;
    PVector position;

    String name;

    RectangularField (float fieldOrientation, int columnNumber, float fieldAlpha) {
        this.fieldOrientation = fieldOrientation;
        this.fieldWidth = width / columnNumber;
        this.fieldHeight = height / columnNumber;
        this.fieldAlpha = fieldAlpha;
        this.position = new PVector(0,0);
    }

    public void updateColumnNumber(int n)
    {
        this.fieldWidth  = width  / n;
        this.fieldHeight = height / n;
    }

    public void drawMe()
    {
        noStroke();
        fill(fieldAlpha);
        rect(position.x, position.y, fieldWidth, fieldHeight);
    }

    public boolean isPixelOnSquare(PVector pos)
    {
        return (pos.x >= position.x && pos.x <= (position.x + fieldWidth) ) && (pos.y >= position.y && pos.y <= (position.y + fieldHeight) ) ? true : false;
    }

    public float getBrightness()
    {
        return fieldAlpha * 2*PI/255 + fieldOrientation;
    }

}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Julien_Gargot_Agents_And_Fields" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
