package at.edu.c02.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * This is the main program entry point. TODO: add new commands when implementing additional features.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        LedController ledController = new LedControllerImpl(new ApiServiceImpl());

        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!input.equalsIgnoreCase("exit"))
        {
            System.out.println("=== LED Controller ===");
            System.out.println("Enter 'demo' to send a demo request");
            System.out.println("Enter 'groupstatus' to show led state");
            System.out.println("Enter 'status' to show led state for specific led");
            System.out.println("Enter 'setled' turn on led");
            System.out.println("Enter 'alloff' turn all of");
            System.out.println("Enter 'spinningled' turn led on one by one");
            System.out.println("Enter 'spinningwheel'");
            System.out.println("Enter 'exit' to exit the program");
            input = reader.readLine();
            if(input.equalsIgnoreCase("demo"))
            {
                ledController.demo();
            }
            else if(input.equalsIgnoreCase("setled"))
            {
                System.out.println("Which LED:");
                input = reader.readLine();
                int led = Integer.parseInt(input);
                System.out.println("Which color:");
                String color = reader.readLine();
                ledController.setLed(led, color, true);
            }
            else if(input.equalsIgnoreCase("alloff"))
            {
                ledController.turnAllOff();
            }
            else if (input.equalsIgnoreCase("groupstatus")) {
                ledController.getGroupLeds();
            }
            else if (input.equalsIgnoreCase("status")) {
                System.out.println("Please specify LED ID:");
                input = reader.readLine();
                ledController.getStatus(Integer.parseInt(input));
            }
            else if (input.equalsIgnoreCase("spinningled")) {
                System.out.println("Which color?");
                String color = reader.readLine();
                System.out.println("How many turns?");
                input  = reader.readLine();
                int durchlauf = Integer.parseInt(input);
                ledController.lauflicht(color, durchlauf);
            }
            else if (input.equalsIgnoreCase("spinningwheel")) {
                System.out.println("How many times?");
                input  = reader.readLine();
                int durchlauf = Integer.parseInt(input);
                ledController.spinningWheel(durchlauf);
            }
        }
    }
}
