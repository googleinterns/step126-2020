import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapData {

  static final Map<String, ArrayList> zipPrecinctMap;

  static {
    zipPrecinctMap = new HashMap<>();
    zipPrecinctMap.put("94103", new ArrayList<>(Arrays.asList("Southern", "Mission")));
    zipPrecinctMap.put("94107", new ArrayList<>(Arrays.asList("Bayview", "Southern")));
    zipPrecinctMap.put(
        "94109", new ArrayList<>(Arrays.asList("Tenderloin", "Central", "Northern")));
    zipPrecinctMap.put("94110", new ArrayList<>(Arrays.asList("Ingleside", "Mission")));
    zipPrecinctMap.put("94112", new ArrayList<>(Arrays.asList("Ingleside", "Taraval")));
    zipPrecinctMap.put("94114", new ArrayList<>(Arrays.asList("Park", "Mission")));
    zipPrecinctMap.put("94116", new ArrayList<>(Arrays.asList("Taraval")));
    zipPrecinctMap.put("94117", new ArrayList<>(Arrays.asList("Park", "Northern")));
    zipPrecinctMap.put("94118", new ArrayList<>(Arrays.asList("Richmond", "Park")));
    zipPrecinctMap.put("94121", new ArrayList<>(Arrays.asList("Richmond")));
    zipPrecinctMap.put("94122", new ArrayList<>(Arrays.asList("Richmond", "Taraval", "Park")));
    zipPrecinctMap.put("94123", new ArrayList<>(Arrays.asList("Northern")));
    zipPrecinctMap.put("94124", new ArrayList<>(Arrays.asList("Bayview")));
    zipPrecinctMap.put("94129", new ArrayList<>(Arrays.asList("Richmond")));
    zipPrecinctMap.put("94131", new ArrayList<>(Arrays.asList("Ingleside", "Park")));
  }

  public ArrayList getPrecincts(String zipcode) {
    return zipPrecinctMap.get(zipcode);
  }
}
