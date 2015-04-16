class PerlinNoiseField
{
    float fieldIntensity; // Intensité du champ de force
    float noiseScale; // 'Géométrie' du bruit

    PerlinNoiseField(float fieldIntensity, float noiseScale) 
    {
        this.fieldIntensity = fieldIntensity;
        this.noiseScale = noiseScale;
    }

    // Méthode permettant d'obtenir la valeur du champ de force à une position donnée
    float getNoiseValue(PVector position)
    {
        return noise(position.x / noiseScale, position.y / noiseScale) * fieldIntensity;
    }
}