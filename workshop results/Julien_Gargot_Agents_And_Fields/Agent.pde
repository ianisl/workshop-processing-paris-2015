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

  void updatePosition()
  {
    previousPosition = position.get();
    position.x += cos(angle) * stepSize;
    position.y += sin(angle) * stepSize;
    if(isPositionResetWhenOutside && isOutsideStetch() > 0) {
      position = new PVector(random(width), random(height));
      previousPosition = position.get();
    }
  }

  int isOutsideStetch()
  {
    if (position.y < 0 ) {
      return 1;
    }
    else if (position.x > width) {
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