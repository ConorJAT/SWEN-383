// Imports needed for Swing UI display.
import java.awt.Font ;
import java.awt.GridLayout ;

import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;

// Imports needed to observe Weather Station.
import java.util.Observer ;
import java.util.Observable ;


public class SwingUI extends JFrame implements Observer {
    // Declare field for the WeatherStation to be observed.
    private final WeatherStation station;

    // Declare fields for temperature display.
    private JLabel kelvinField;
    private JLabel celsiusField;
    private JLabel fahrenheitField;
    private JLabel hgInchesField;
    private JLabel millibarsField;

    // Declare and instantiate new font for display.
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 42) ;
    
    public SwingUI(WeatherStation station) {
        super("Weather Station (SwingUI)") ;

        // Initialize the weather station and set "this" as its observer.
        this.station = station;
        this.station.addObserver(this);

        /*
         * WeatherStation frame is a grid of 1 row by an indefinite
         * number of columns.
         */
        this.setLayout(new GridLayout(1,0)) ;

        kelvinField = setNewJPanel(kelvinField, " Kelvin ");                // Set up Kelvin display.
        celsiusField = setNewJPanel(celsiusField, " Celcius ");             // Set up Celcius display.
        fahrenheitField = setNewJPanel(fahrenheitField, " Fahrenheit ");    // Set up Fahrenheit display.
        hgInchesField = setNewJPanel(hgInchesField, " Inches ");            // Set up Mercury Inches display.
        millibarsField = setNewJPanel(millibarsField, " Millibars ");       // Set up Millibars display.

        /*
         * Set up the frame's default close operation pack its elements,
         * and make the frame visible.
         */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        this.pack() ;
        this.setVisible(true) ;
    }

    /*
     * Simplified helper function, used to more efficiently create the various
     * JPanels for the Weather Station display. Returns a JLabel to update the
     * local JLabel variables (DRY Design).
     */
    public JLabel setNewJPanel(JLabel label, String title) {
        JPanel panel = new JPanel(new GridLayout(2,1)) ;
        this.add(panel);
        createLabel(title, panel);
        return createLabel("", panel);
    }

    /*
     * Simplified helper function, used to more efficiently format the 
     * Weather Station data, rather than having each one being formatted
     * in their own function (DRY Design).
     */
    public void setWeatherJLabel(JLabel label, double value) {
        label.setText(String.format("%6.2f", value));
    }

    /*
     * Create a Label with the initial value <title>, place it in
     * the specified <panel>, and return a reference to the Label
     * in case the caller wants to remember it.
     */
    private JLabel createLabel(String title, JPanel panel) {
        JLabel label = new JLabel(title) ;

        label.setHorizontalAlignment(JLabel.CENTER) ;
        label.setVerticalAlignment(JLabel.TOP) ;
        label.setFont(labelFont) ;
        panel.add(label) ;

        return label ;
    }
    
    public void update(Observable obs, Object ignore) {
        /*
         * Check for spurious updates from unrelated objects.
         */
        if( station != obs ) {
            return ;
        }

        /*
         * Retrieve and print the temperatures.
         */
        this.setWeatherJLabel(kelvinField, station.getKelvin());
        this.setWeatherJLabel(celsiusField, station.getCelsius());
        this.setWeatherJLabel(fahrenheitField, station.getFahrenheit());
        this.setWeatherJLabel(hgInchesField, station.getPressureInches());
        this.setWeatherJLabel(millibarsField, station.getPressureMillibars());
    }
}
