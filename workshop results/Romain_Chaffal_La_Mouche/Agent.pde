class Agent {

  float stepSize;
  PVector position;
  PVector previousPosition;
  float angle;
  boolean isPositionResetWhenOutside;


  Agent()
  {
    position = new PVector(random(width), random(height));
    previousPosition = position.get();
    stepSize = 0.5 ;
    isPositionResetWhenOutside = true;
    //angle = random(2 * PI);
  }

  //surcharge 
  Agent(PVector position)
  {
    this(); // Appel du constructeur par défaut pour initialiser tous les attributs
    this.position = position; // Mise à jour de l'attribut position avec l'argument 'position' passé au constructeur. Comme l'argument du constructeur et l'attribut ont le même nom, on identifie l'attribut en le faisant précéder de 'this.'
    previousPosition = position.get();
  }
  
  void updatePosition() {
    previousPosition = position.get(); 
    //position.x  -= cos(angle) * stepSize;
   // position.y  -= sin(angle) *  stepSize;

    if ( isPositionResetWhenOutside && isOutsideSketch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }
  
   void updatePositionPoint() {
    previousPosition = position.get(); 
    position.x  -= cos(angle) * stepSize ;
    position.y  -= sin(angle) *  stepSize ;

    if ( isPositionResetWhenOutside && isOutsideSketch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }

  int isOutsideSketch()
  {
    if (position.y < 0) 
    {
      return 1;
    } else if (position.x > width) 
    {
      return 2;
    } else if (position.y > height) 
    {
      return 3;
    } else if (position.x < 0) 
    {
      return 4;
    } else
    {
      return 0;
    }
  }
}

