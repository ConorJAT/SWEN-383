
/*
 * Class for a simple computer based weather station that reports the current
 * temperature (in Celsius) every second. The station is attached to a
 * sensor that reports the temperature as a 16-bit number (0 to 65535)
 * representing the Kelvin temperature to the nearest 1/100th of a degree.
 *
 * This class is implements Runnable so that it can be embedded in a Thread
 * which runs the periodic sensing.
 */

import java.util.Scanner;

public class WeatherStation implements Runnable {

    private final KelvinTempSensor sensor ; // Temperature sensor.

    private final AWTUI awtui;
    
    private final SwingUI swingui;

    private final long PERIOD = 1000 ;      // 1 sec = 1000 ms.

    private final int userChoice;

    /*
     * When a WeatherStation object is created, it in turn creates the sensor
     * object it will use.
     */
    public WeatherStation(int userChoice) {
        sensor = new KelvinTempSensor() ;
        this.userChoice = userChoice;

        if (userChoice == 1) {
            awtui = new AWTUI();
            swingui = null;
        } else if (userChoice == 2) {
            awtui = null;
            swingui = new SwingUI();
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
            
            if (userChoice == 1) {
                awtui.celsiusField.setText(String.format("%6.2f", celsius));
                awtui.kelvinField.setText(String.format("%6.2f", reading / 100.0));
            } else if (userChoice == 2) {
                swingui.setKelvinJLabel(reading / 100.0);
                swingui.setCelsiusJLabel(celsius);
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
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        
        System.out.println("Welcome to the RIT Weather Station!");
        System.out.println("How would you like your data displayed?");
        System.out.println("(AWT = 1 | Swing = 2 | Terminal = 3)");
        while (userChoice <= 0 || userChoice >= 4) {  
            
            
            try {
                System.out.print("Your choice: ");
                userChoice = scanner.nextInt();
                if (userChoice <= 0 || userChoice >= 4) { 
                    System.out.println("Invalid option! Please choose again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid option! Please choose again.");
                scanner = new Scanner(System.in);
            }
        }

        WeatherStation ws = new WeatherStation(userChoice) ;
        Thread thread = new Thread(ws) ;
        thread.start() ;
    }
}
