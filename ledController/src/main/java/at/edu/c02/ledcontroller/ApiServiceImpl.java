package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class should handle all HTTP communication with the server.
 * Each method here should correspond to an API call, accept the correct parameters and return the response.
 * Do not implement any other logic here - the ApiService will be mocked to unit test the logic without needing a server.
 */
public class ApiServiceImpl implements ApiService {
    /**
     * This method calls the `GET /getLights` endpoint and returns the response.
     * TODO: When adding additional API calls, refactor this method. Extract/Create at least one private method that
     * handles the API call + JSON conversion (so that you do not have duplicate code across multiple API calls)
     *
     * @return `getLights` response JSON object
     * @throws IOException Throws if the request could not be completed successfully
     */

    private String secret;

    public ApiServiceImpl() {
        try {
            Path path = Paths.get("secret.txt");
            String content = Files.readString(path);
            this.secret = content;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getLights() throws IOException
    {
        String urlString = "https://balanced-civet-91.hasura.app/api/rest/getLights";
        BufferedReader reader = getBufferedReader(urlString);
        // Save the response in this StringBuilder
        StringBuilder sb = new StringBuilder();

        int character;
        // Read the response, character by character. The response ends when we read -1.
        while((character = reader.read()) != -1) {
            sb.append((char) character);
        }

        String jsonText = sb.toString();
        // Convert response into a json object
        return new JSONObject(jsonText);
    }

    @Override
    public JSONObject getLight(int id) throws IOException
    {
        String urlString = "https://balanced-civet-91.hasura.app/api/rest/lights/" + id;
        BufferedReader reader = getBufferedReader(urlString);
        // Save the response in this StringBuilder
        StringBuilder sb = new StringBuilder();

        int character;
        // Read the response, character by character. The response ends when we read -1.
        while((character = reader.read()) != -1) {
            sb.append((char) character);
        }

        String jsonText = sb.toString();
        // Convert response into a json object
        return new JSONObject(jsonText);
    }

    private BufferedReader getBufferedReader(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // and send a GET request
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Hasura-Group-ID", this.secret);
        // Read the response code
        int responseCode = connection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            // Something went wrong with the request
            throw new IOException("Error: getLights request failed with response code " + responseCode);
        }

        // The request was successful, read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return reader;
    }

    public Boolean setLed(int led, String color,boolean state) throws IOException{
        URL url = new URL("https://balanced-civet-91.hasura.app/api/rest/setLight");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("X-Hasura-Group-ID", this.secret);
        connection.setDoOutput(true);
        String jsonInputString = String.format("{\"id\": \"%s\", \"color\": \"%s\", \"state\": %s}", led, color, state);
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        return true;
    }
    public JSONArray getGroupLeds() throws IOException
    {
        JSONObject allLights = getLights();
        if (allLights == null) return null;
        JSONArray array = allLights.getJSONArray("lights");

        if (allLights.isEmpty()) return null;

        JSONArray returnArray = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject objectGroup = object.getJSONObject("groupByGroup");
            if (objectGroup.getString("name").equals("C")) {
                System.out.println("LED " + object.getInt("id") +  " is currently " + (object.getBoolean("on") ? "on" : "off") + ". Color: " + object.getString("color") + ".");
                returnArray.put(object);
            }
        }
        return returnArray;

    }

    public void turnAllOff() throws IOException{
        int[] leds = new int[8];
        for(int i = 0; i < leds.length; i++){
            leds[i] = 20 + i;
        }
        for (int led : leds) {
            setLed(led, "#000", false);
        }
    }

    public void lauflicht(String color, int durchlauf) throws IOException, InterruptedException{

        turnAllOff();
        int[] leds = new int[8];
        for(int i = 0; i < leds.length; i++){
            leds[i] = 20 + i;
        }
        for(int i = 0; i < durchlauf; i++){
            for (int led : leds) {
                setLed(led, color, true);
                Thread.sleep(1000);
                setLed(led, color, false);
            }
        }
        turnAllOff();
    }

    public void spinningWheel(int durchlauf) throws IOException, InterruptedException{
        JSONArray ledsStatus = getGroupLeds();
        JSONArray onLed= new JSONArray();
        for(int i = 0; i < ledsStatus.length(); i++){
            JSONObject led = ledsStatus.getJSONObject(i);
            if(led.getBoolean("on")){
                onLed.put(led);
            }
        }

        int[] leds = new int[8];
        for(int i = 0; i < leds.length; i++){
            leds[i] = 20 + i;
        }

        for(int i = 0; i < durchlauf; i++){
            for(int j = 0; j < leds.length; j++){

            }
        }

    }
}
