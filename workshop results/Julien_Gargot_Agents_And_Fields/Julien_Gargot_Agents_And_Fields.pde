import controlP5.*;

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
color agentColor = color(255);
float agentAlpha = 90;
float fieldAlpha = 128;
color backgroundColor = color(0);

// Variables
ArrayList<RectangularField> fields;
ArrayList<Agent> agents;
ControlP5 cp5;
boolean displayBackground = false;
float fieldWidth;
float fieldHeight;


void setup() {
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

void draw()
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

void gridStepping(int inputValue)
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

void agentCount(int inputValue) {
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

void agentStepSize(float inputValue)
{
    for (Agent a : agents)
    {
        a.stepSize = inputValue;
    }
}

void displayBackground() { // Dev purpose only.
    displayBackground = !displayBackground;
}

void mode() {
    mode = mode == "random" ? "breath" : "random";
}