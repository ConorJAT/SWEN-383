
/*
 * Class for a simple computer based weather station that reports the current
 * temperature (in Celsius) every second. The station is attached to a
 * sensor that reports the temperature as a 16-bit number (0 to 65535)
 * representing the Kelvin temperature to the nearest 1/100th of a degree.
 *
 * This class is implements Runnable so that it can be embedded in a Thread
 * which runs the periodic sensing.
 */

import java.util.Scanner;       // Scanner util for user input.

public class WeatherStation implements Runnable {

    private final KelvinTempSensor sensor ; // Temperature sensor.

    private final AWTUI awtui;              // AWT UI Display Object.
    
    private final SwingUI swingui;          // Swing UI Display Object.

    private final long PERIOD = 1000 ;      // 1 sec = 1000 ms.

    private final int userChoice;           // Choice of user display.

    /*
     * When a WeatherStation object is created, it in turn creates the sensor
     * object it will use.
     */
    public WeatherStation(int userChoice) {
        // Always instantiate a new TempSensor and userChoice.
        sensor = new KelvinTempSensor() ;
        this.userChoice = userChoice;

        // 1 == AWT Display (Swing is null)
        if (userChoice == 1) {
            awtui = new AWTUI();
            swingui = null;
        // 2 == Swing Display (AWT is null)
        } else if (userChoice == 2) {
            awtui = null;
            swingui = new SwingUI();
        // 3 == Terminal Display (both AWT and Swing are null)
        } else {
            awtui = null;
            swingui = null;
        }
    }

    /*
     * The "run" method called by the enclosing Thread object when started.
     * Repeatedly sleeps a second, acquires the current temperature from
     * its sensor, and reports this as a formatted output string.
     */
    public void run() {
        int reading ;           // actual sensor reading.
        double celsius ;        // sensor reading transformed to celsius
        final int KTOC = -27315 ;   // Convert raw Kelvin reading to Celsius

        while( true ) {
            try {
                Thread.sleep(PERIOD) ;
            } catch (Exception e) {}    // ignore exceptions

            reading = sensor.reading() ;
            celsius = (reading + KTOC) / 100.0 ;
            /*
             * System.out.printf prints formatted data on the output screen.
             *
             * Most characters print as themselves.
             *
             * % introduces a format command that usually applies to the
             * next argument of printf:
             *   *  %6.2f formats the "celsius" (2nd) argument in a field
             *      at least 6 characters wide with 2 fractional digits.
             *   *  The %n at the end of the string forces a new line
             *      of output.
             *   *  %% represents a literal percent character.
             *
             * See docs.oracle.com/javase/tutorial/java/data/numberformat.html
             * for more information on formatting output.
            */
            
            // If AWT, update label texts directly via pubic fields.
            if (userChoice == 1) {
                awtui.celsiusField.setText(String.format("%6.2f", celsius));
                awtui.kelvinField.setText(String.format("%6.2f", reading / 100.0));

            // If Swing, update label texts via public helper functions.
            } else if (userChoice == 2) {
                swingui.setKelvinJLabel(reading / 100.0);
                swingui.setCelsiusJLabel(celsius);
            // If Terminal, just print the line of temps into the Terminal.
            } else {
                System.out.printf("Reading is %6.2f degrees C (and " + String.format("%6.2f", reading / 100.0) + " degrees K)%n", celsius) ;
            }
        }
    }

    /*
     * Initial main method.
     *      Create the WeatherStation (Runnable).
     *      Embed the WeatherStation in a Thread.
     *      Start the Thread.
     */
    public static void main(String[] args) {
        // Declare and instantiate new scanner and set default value to zero.
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        
        System.out.println("Welcome to the RIT Weather Station!");
        System.out.println("How would you like your data displayed?");
        System.out.println("(AWT = 1 | Swing = 2 | Terminal = 3)");

        // Keep user in the loop until they enter a valid input.
        while (userChoice <= 0 || userChoice >= 4) {  
            try {
                System.out.print("Your choice: ");

                // User input MUST be an int.
                userChoice = scanner.nextInt();

                // If input is an int, but out of range, let them know and keep in loop.
                if (userChoice <= 0 || userChoice >= 4) { 
                    System.out.println("Invalid option! Please choose again.");
                }
            } catch (Exception e) {
                // If input is NOT an int, let thme know and keep in loop.
                System.out.println("Invalid option! Please choose again.");
                scanner = new Scanner(System.in);
            }
        }

        WeatherStation ws = new WeatherStation(userChoice) ;
        Thread thread = new Thread(ws) ;
        thread.start() ;
    }
}
