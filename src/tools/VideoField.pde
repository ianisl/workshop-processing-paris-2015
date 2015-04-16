import processing.video.*;

class VideoField
{
    float fieldIntensity; // Intensité du champ de force
    Capture capture; // Flux vidéo
    PImage image; // Image stockée en mémoire à partir de la vidéo (géométrie du champ de force)
    boolean isImageLoaded; // 'true' si l'image en mémoire a été remplie au moins une fois à l'aide de la méthode 'update'

    // Constructeur initialisant le flux vidéo à partir de l'entrée vidéo par défaut
    VideoField(float fieldIntensity, PApplet p) 
    {
        this.fieldIntensity = fieldIntensity;
        this.capture = new Capture(p, width, height);
        this.image = createImage(width, height, RGB);
        this.isImageLoaded = false;
        this.capture.start();
    }

    // Méthode permettant de mettre à jour l'image en mémoire à partir du flux vidéo
    void update()
    {
        if (capture.available())
        {
            isImageLoaded = true;
            capture.read();
            capture.loadPixels();
            image.loadPixels();
            for (int i = 0; i < image.pixels.length; i++) 
            {
              image.pixels[i] = capture.pixels[i]; 
            }
            image.updatePixels();
        }
    }

    // Méthode permettant d'obtenir la luminosité de l'image en mémoire à une position donnée
    float getBrightness(PVector position)
    {
        color c = getColor(position);
        return brightness(c) * fieldIntensity;
    }


    // Méthode permettant d'obtenir
    // la couleur de l'image en mémoire à une position donnée.
    color getColor(PVector position)
    {
        return image.get(int(position.x/width * image.width), int(position.y/height * image.height));
    }

}