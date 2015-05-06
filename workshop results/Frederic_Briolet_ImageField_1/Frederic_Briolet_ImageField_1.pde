// Paramètres
int agentCount = 5000;
float agentSize = 1.5;
float agentAlpha = 1;
float fieldIntensity = 0.03;
float blurLevel = 1;
float noiseScale = 100;
float agentStepSize = 2;
color mouseColor = color(255, 0, 0);
int mouseColorOpacity = 15;

// Variables
PerlinNoiseField fieldPerlin;
ImageField field;
ArrayList<Agent> agents;

void setup() 
{
  size(1000, 765);
  smooth(0);
  background(0);
  field = new ImageField(fieldIntensity, "balls.jpg");
  fieldPerlin = new PerlinNoiseField(fieldIntensity, noiseScale);
  field.blur(blurLevel);
  agents = new ArrayList<Agent>();
  for (int i = 0; i < agentCount; i++)
  {
    Agent a = new Agent();
    a.stepSize = agentStepSize;
    agents.add(a);
  } 
}

void draw() 
{
  for (Agent a : agents)
  {
    a.angle = field.getBrightness(a.position);
    a.updatePosition();
  }    
  strokeWeight(agentSize);
  for (Agent a : agents)
  {
    stroke(255, 3);
    if(a.position.x > (mouseX-random(200)) && a.position.x < (mouseX+random(200)) && a.position.y < (mouseY+random(200)) && a.position.y > (mouseY-random(200))){
      stroke(mouseColor, mouseColorOpacity);
      fieldIntensity = random(400);
      a.position.x += 10;
    }
    line(a.previousPosition.x, a.previousPosition.y, a.position.x, a.position.y);
  }
  
}

// Interactive 

void keyPressed() {
  if (key == 'j') {
    save("recorded/" + getTimestamp() + ".jpg");
  }
  if (key == ' ') {
    noiseScale = random(200);
    fieldIntensity = random(0.02,0.04);
    background(0);
    setup();
  }
  if (key == 'a') {
    mouseColor = color(0,0,255); // bleu
  }
  if (key == 'z') {
    mouseColor = color(255,0,0); // rouge
  }
  if (key == 'e') {
    mouseColor = color(255,215,0); // or
  }
  if (key == 'r') {
    mouseColor = color(119,181,254); // bleu ciel
  }
  if (key == 't') {
    mouseColor = color(144,40,59); // amarante
  }
  if (key == 'y') {
    mouseColor = color(158,14,64); // pourpre
  }
  if (key == 'u') {
    mouseColor = color(53,122,183); // céruléen
  }
  if (key == 'i') {
    mouseColor = color(204,85,0); // orange brulée
  }
  if (key == 'o') {
    mouseColor = color(100,155,136); // glauque
  }
  if (key == 'p') {
    mouseColor = color(0,142,142); // bleu sarcelle
  }
  if (key == 'q') {
    mouseColor = color(1,121,111); // vert pin
  }
  if (key == 's') {
    mouseColor = color(106,69,93); // colombin
  }
  if (key == 'd') {
    mouseColor = color(239,216,7); // Jaune d'or
  }
  if (key == 'w') {
    mouseColor = color(255,255,255); // Blanc
  }
  if (key == 'x') {
    mouseColor = color(0,0,0); // Noir
    println( mouseColor );
  }
  if (keyCode == UP ) {
    mouseColorOpacity += 2; // opacité couleur + 2
  }
  if (keyCode == DOWN ) {
    mouseColorOpacity -= 2; // opacité couleur - 2
  }
  if (key == 'n') {
    blurLevel += 1; // blurLevel = lissage
    println(blurLevel);
  }
  if (key == 'b') {
    blurLevel -= 1; // blurLevel = lissage;
    println(blurLevel);
  }
}
