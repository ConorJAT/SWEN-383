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

    // Declare and instantiate new font for display.
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 72) ;
    
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

        /*
         * There are two panels, one each for Kelvin and Celsius, added to the
         * frame. Each Panel is a 2 row by 1 column grid, with the temperature
         * name in the first row and the temperature itself in the second row.
         */
        JPanel panel ;

        /*
         * Set up Kelvin display.
         */
        panel = new JPanel(new GridLayout(2,1)) ;
        this.add(panel) ;
        createLabel(" Kelvin ", panel) ;
        kelvinField = createLabel("", panel) ;

        /*
         * Set up Celsius display.
         */
        panel = new JPanel(new GridLayout(2,1)) ;
        this.add(panel) ;
        createLabel(" Celsius ", panel) ;
        celsiusField = createLabel("", panel) ;

        /*
         * Set up the frame's default close operation pack its elements,
         * and make the frame visible.
         */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        this.pack() ;
        this.setVisible(true) ;
    }

    /*
     * Set the label holding the Kelvin temperature.
     */
    public void setKelvinJLabel(double temperature) {
        kelvinField.setText(String.format("%6.2f", temperature)) ;
    }

    /*
     * Set the label holding the Celsius temperature.
     */
    public void setCelsiusJLabel(double temperature) {
        celsiusField.setText(String.format("%6.2f", temperature)) ;
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
        this.setKelvinJLabel(station.getKelvin());
        this.setCelsiusJLabel(station.getCelsius());
    }

    public static void main(String[] args) {
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);
        SwingUI swing = new SwingUI(ws);

        thread.start() ;
    }
}
