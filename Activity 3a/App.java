import java.util.Scanner;

// Rather than having each UI have its own "Main" and thread.start(), App.java
// centralizes the user decision and can starts the thread for both TextUI
// and SwingUI.
public class App {
    public static void main(String[] args) {
        // Declare and instantiate new scanner and set default value to zero.
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;

        System.out.println("Welcome to the RIT Weather Station 2.0!");
        System.out.println("Please select a display option for your data.");
        System.out.println("(1 = Terminal | 2 = SwingUI | 3 = Both)");

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

        // Declare and instantiate new WeatherStation and thread for the app.
        WeatherStation ws = new WeatherStation();
        Thread thread = new Thread(ws);

        // Use user choice information to setup UI.
        if (userChoice == 1) { 
            new TextUI(ws); 
        } else if (userChoice == 2) { 
            new SwingUI(ws); 
        } else {
            new TextUI(ws);
            new SwingUI(ws);
        }

        // Start the app.
        thread.start() ;
    }
}
