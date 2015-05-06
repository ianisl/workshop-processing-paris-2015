class Agent 
{
    PVector position; 
    PVector previousPosition;
    float stepSize;
    float angle;    
     
    Agent() 
    {
        refreshAgent();
        stepSize = 1;
    }

    Agent(PVector position)
    {
        this();
        //this.position = position;  
        previousPosition = position.get();
    }
    void refreshAgent()
    {
      position = new PVector(random(10, width-10), random(10, height-10));
      previousPosition = position.get();
      angle = random(2 * PI);
    } 
    void updatePosition() 
    {
        
        int whereAgentIsGone = isOutsideSketch();

        if (whereAgentIsGone > 0 ) 
        {
            //position = new PVector(random(width), random(height));         
            
            //float newAngle = angle;
            //println(whereAgentIsGone);

            switch (whereAgentIsGone){
              case(1):
                //newAngle = -angle;
                position.y = height;
                break;  
              
              case(2):
                //newAngle = PI - angle;
                position.x =  0;
                break;
                
              case(3):
                //newAngle = -angle;
                position.y = 0;
                break;
               
              case(4):
                //newAngle = PI - angle;
                position.x =  width;
                break;
                
              default:
                //println("No agent outside");
                break;
            }
          //angle = newAngle;
        }
        previousPosition = position.get();
        position.x += cos(angle) * stepSize;
        position.y += sin(angle) * stepSize;

    }
    
    int isOutsideSketch()
    {
        if (position.y < 0 ) 
        {
            return 1;
        } 
        else if (position.x > width ) 
        {
            return 2;
        } 
        else if (position.y > height )
        {
            return 3;
        }
        else if (position.x < 0 )
        {
            return 4;
        } 
        else
        {
            return 0;
        }
    }
}
