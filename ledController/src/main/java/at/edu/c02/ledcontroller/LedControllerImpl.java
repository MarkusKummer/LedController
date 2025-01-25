package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class handles the actual logic
 */
public class LedControllerImpl implements LedController {
    private final ApiService apiService;

    public LedControllerImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public void demo() throws IOException
    {
        // Call `getLights`, the response is a json object in the form `{ "lights": [ { ... }, { ... } ] }`
        JSONObject response = apiService.getLights();
        // get the "lights" array from the response
        JSONArray lights = response.getJSONArray("lights");
        // read the first json object of the lights array
        JSONObject firstLight = lights.getJSONObject(0);
        // read int and string properties of the light
        System.out.println("First light id is: " + firstLight.getInt("id"));
        System.out.println("First light color is: " + firstLight.getString("color"));
    }

    public void setLed(int led, String color,boolean status)throws IOException
    {
        apiService.setLed(led, color, status);
    }

    public void getGroupLeds() throws IOException
    {
        JSONObject allLights = apiService.getLights();
        if (allLights == null) return;
        JSONArray array = allLights.getJSONArray("lights");

        if (allLights.isEmpty()) return;

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject objectGroup = object.getJSONObject("groupByGroup");
            if (objectGroup.getString("name").equals("C")) {
                System.out.println("LED " + object.getInt("id") +  " is currently " + (object.getBoolean("on") ? "on" : "off") + ". Color: " + object.getString("color") + ".");
            }
        }

    }

    @Override
    public boolean getStatus(int id) throws IOException {
        JSONObject allLights = apiService.getLight(id);
        if (allLights == null) return false;
        JSONArray array = allLights.getJSONArray("lights");

        if (allLights.isEmpty()) return false;

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getInt("id") == id) {
                System.out.println("LED " + object.getInt("id") +  " is currently " + (object.getBoolean("on") ? "on" : "off") + ". Color: " + object.getString("color") + ".");
                return object.getBoolean("on");
            }
        }
        return false;
    }

    public void turnAllOff() throws IOException {
        apiService.turnAllOff();
    }
    public void lauflicht(String color, int durchlauf) throws IOException, InterruptedException{
        apiService.lauflicht(color, durchlauf);
    }

}
