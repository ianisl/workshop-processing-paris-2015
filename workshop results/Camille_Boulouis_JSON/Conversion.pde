float lngToXWorld(float lon, float projectionSize)
{
    float radius = projectionSize / (2 * PI);
    float falseEasting = -1.0 * projectionSize / 2.0;
    return (radius * lon * PI / 180) - falseEasting;
  }

// Convert latitude to y world coordinate
float latToYWorld(float lat, float projectionSize) 
{
    float radius = projectionSize / (2 * PI);
    float falseNorthing = projectionSize / 2;
    return ((radius / 2 * log((1 + sin(lat * PI / 180)) / (1 - sin(lat * PI / 180)))) - falseNorthing) * (-1);
}
