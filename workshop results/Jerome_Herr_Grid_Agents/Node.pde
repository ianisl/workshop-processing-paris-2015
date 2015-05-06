class Node {

  PVector position_start;
  PVector position_updated;
  float sz=3;
  float sz_upd;
  float m; // multiplicateur
  float t; // time
  boolean inDistance;
  int fc; // frameCount at a certain moment in time
  float incr, timer;

  Node(PVector start) {
    position_start = start;
    position_updated = start;
    sz_upd=sz;
  }

  void run() {
    update_dist();
    //update_sz();
    display_sz();
    returnToStart();
  }

  void update_dist() {
    for (Agent a : agents) {
      float distance = dist(a.position.x, a.position.y, position_updated.x, position_updated.y);
      if (distance>minDist && distance<maxDist) {
        m = map(distance, minDist, maxDist, 3, 0);
        incr = map(distance, minDist, maxDist, 0.03, 0.01);
        timer = map(distance, minDist, maxDist, 40, 10);
        fc = frameCount;
      } else {
        m = 0;
        //inDistance = false;
      }
      PVector p = PVector.sub(position_updated, a.position);
      p.normalize();
      p.mult(m);
      position_updated = PVector.add(position_updated, p);
    }
  }

  void returnToStart() {
    if (frameCount>fc+timer) {
      position_updated.lerp(position_start, t);
      if (t<.95) t+= incr;
    }
  }

  void update_sz() {
    for (Agent a : agents) {
      float distance = dist(a.position.x, a.position.y, position_updated.x, position_updated.y);
      if (distance<maxDist) {
        sz_upd = map(distance, 0, maxDist, 10, 5);
      } else {
        sz_upd = sz;
      }
    }
  }

  void display_sz() {
    fill(0);
    noStroke();
    ellipse(position_updated.x, position_updated.y, sz_upd, sz_upd);
  }
}

