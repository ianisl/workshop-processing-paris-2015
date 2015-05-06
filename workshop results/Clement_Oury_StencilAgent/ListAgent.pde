import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListAgent extends ArrayList<AgentGeom> {
  
  ListAgent() {
  }
  
  public void orderByZOrder() {
    Collections.sort(this, new CustomComparator());
  }
  
  public class CustomComparator implements Comparator<AgentGeom> {
    public int compare(AgentGeom arg0, AgentGeom arg1) {      
      return (int) (arg1.position.z-arg0.position.z);
    }
  }

}

