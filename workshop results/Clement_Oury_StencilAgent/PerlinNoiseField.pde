import processing.core.PApplet;
import processing.core.PVector;

public class PerlinNoiseField {
  PApplet p;
  public float fieldIntensity; //intensitÃ©
  public float noiseScale; // gÃ©omÃ©trie
  
  public PerlinNoiseField(PApplet parent, float fieldIntensity, float noiseScale) {
    this.p = parent;
    this.fieldIntensity = fieldIntensity;
    this.noiseScale = noiseScale; 
  }
  
  public float getNoiseValue(PVector position) {
    return p.noise(position.x / noiseScale, position.y / noiseScale)*fieldIntensity;
  }
  
}

