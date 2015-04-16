// Paramètres
float agentSize = 10;

// Variables
PVector position; // Position de l'agent
PVector previousPosition; // Stockage de la position précédente (pour le dessin)
float stepSize; // Incrément de déplacement (= vitesse de base de l'agent)
float angle; // Angle de déplacement

void setup() 
{
    size(800, 800);
    position = new PVector(random(width), random(height)); // Position aléatoire
    previousPosition = position.get(); // Attention à bien copier le PVector avec la méthode 'get()'
    angle = random(2 * PI); // angle aléatoire
    stepSize = 1;
}

void draw()
{
    // Mise à jour de la position de l'agent
    previousPosition = position.get(); // Sauvegarde de la position précédente
    position.x += cos(angle) * stepSize; // L'agent avance sur une distance égale à 'stepSize' à partir de sa position actuelle, selon un angle 'angle'
    position.y += sin(angle) * stepSize;
    if (isOutsideSketch() > 0) 
    {
        position = new PVector(random(width), random(height)); // Si l'agent sort du sketch, on lui attribue une nouvelle position aléatoire
        previousPosition = position.get();
    }
    // Dessin
    background(255); // On efface l'écran à chaque itération
    stroke(0);
    strokeWeight(agentSize);
    line(previousPosition.x, previousPosition.y, position.x, position.y);       
}

// Une *fonction* permettant de vérifier si l'agent est sorti des limites de l'espace du sketch (+ marges)
// La fonction renvoie les valeurs suivantes :
// 0: l'agent n'est pas sorti des limites de l'espace du sketch
// 1: l'agent est sorti par le haut
// 2: l'agent est sorti par la droite
// 3: l'agent est sorti par le bas
// 4: l'agent est sorti par la gauche
int isOutsideSketch()
{
    if (position.y < 0) 
    {
        return 1;
    } 
    else if (position.x > width) 
    {
        return 2;
    } 
    else if (position.y > height) 
    {
        return 3;
    }
    else if (position.x < 0) 
    {
        return 4;
    } 
    else
    {
        return 0;
    }
}