class RectangularField {

    float fieldOrientation;
    float fieldWidth;
    float fieldHeight;
    float fieldAlpha;
    PVector position;

    String name;

    RectangularField (float fieldOrientation, int columnNumber, float fieldAlpha) {
        this.fieldOrientation = fieldOrientation;
        this.fieldWidth = width / columnNumber;
        this.fieldHeight = height / columnNumber;
        this.fieldAlpha = fieldAlpha;
        this.position = new PVector(0,0);
    }

    public void updateColumnNumber(int n)
    {
        this.fieldWidth  = width  / n;
        this.fieldHeight = height / n;
    }

    public void drawMe()
    {
        noStroke();
        fill(fieldAlpha);
        rect(position.x, position.y, fieldWidth, fieldHeight);
    }

    public boolean isPixelOnSquare(PVector pos)
    {
        return (pos.x >= position.x && pos.x <= (position.x + fieldWidth) ) && (pos.y >= position.y && pos.y <= (position.y + fieldHeight) ) ? true : false;
    }

    public float getBrightness()
    {
        return fieldAlpha * 2*PI/255 + fieldOrientation;
    }

}