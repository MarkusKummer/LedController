package at.edu.c02.ledcontroller;

import org.json.JSONObject;

import java.io.IOException;

public interface ApiService {
    JSONObject getLights() throws IOException;
    Boolean setLed(int led, String color,boolean status) throws IOException;
    void turnAllOff() throws IOException;
    JSONObject getLight(int id) throws IOException;
    void lauflicht(String color, int durchlauf) throws IOException, InterruptedException;
    void spinningWheel(int durchlauf) throws IOException, InterruptedException;

}
