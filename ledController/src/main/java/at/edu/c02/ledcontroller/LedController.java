package at.edu.c02.ledcontroller;

import org.json.JSONArray;

import java.io.IOException;

public interface LedController {
    void demo() throws IOException;
    void setLed(int led, String color,boolean status)throws IOException;
    void turnAllOff() throws IOException;
    void getGroupLeds() throws IOException;
    boolean getStatus(int id) throws IOException;
    void lauflicht(String color, int durchlauf) throws IOException, InterruptedException;
    void spinningWheel(int durchlauf) throws IOException, InterruptedException;
}
