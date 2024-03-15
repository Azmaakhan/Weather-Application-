import java.sql.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static HashMap<String, String> userCredentials = new HashMap<>();
    static boolean loggedIn = false;
    static String currentUser = "";

    static int storageChoice = -1;

    public static void main(String[] args) {
        System.out.println("Welcome to the Weather App!");

        // Prompt user to choose storage type
        System.out.println("Please choose a storage type:");
        System.out.println("1. Text File");
        System.out.println("2. SQLite Database");
        System.out.print("Enter your choice (1 or 2): ");
        storageChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (storageChoice) {
            case 1:
                useTextFileStorage();
                break;
            case 2:
                useSQLiteDatabase();
                break;
            default:
                System.out.println("Invalid choice. Exiting program.");
                System.exit(1);
        }

        if (!loggedIn) {
            showLoginMenu();
        } else {
            showMainMenu();
        }
    }

    static void useTextFileStorage() {
        System.out.println("Using Text File Storage...");
        loadUserDataFromFile();
    }

    static void useSQLiteDatabase() {
        System.out.println("Using SQLite Database Storage...");
        initializeDatabase();
        loadUserDataFromDatabase();
    }

    static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:user_data.db");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS users (email TEXT PRIMARY KEY, password TEXT)");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error initializing SQLite database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void loadUserDataFromFile() {
        try {
            File file = new File("user_data.txt");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                // Assuming email is unique
                if (parts.length == 2) {
                    addUserCredentials(parts[0], parts[1]);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading user data from file: " + e.getMessage());
        }
    }

    static void loadUserDataFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:user_data.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                addUserCredentials(email, password);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error loading user data from database: " + e.getMessage());
        }
    }

    static void addUserCredentials(String email, String password) {
        // Add user credentials to memory
        // You may want to modify this method to store credentials based on storage type
        Main.userCredentials.put(email, password);
    }

    static void showLoginMenu() {
        System.out.println("Welcome to the Weather App!");

        while (!loggedIn) {
            System.out.println("\nLogin/Register Menu:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Please enter your choice (1-2): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 for Login or 2 for Register.");
            }
        }
        showMainMenu();
    }

    static void login() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (userCredentials.containsKey(email) && userCredentials.get(email).equals(password)) {
            System.out.println("Login successful!");
            loggedIn = true;
            currentUser = email;
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    static void register() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        if (userCredentials.containsKey(email)) {
            System.out.println("Email already exists. Please choose another email.");
            return;
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        userCredentials.put(email, password);
        loggedIn = true;
        currentUser = email;
        System.out.println("Registration successful!");
    }

    static void showMainMenu() {
        System.out.println("Welcome to the Weather App!");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. View Weather by Long/Lat");
            System.out.println("2. View Weather by City/Country");
            System.out.println("3. Show Current Weather");
            System.out.println("4. Show 5 Days Forecast");
            System.out.println("5. Check Air Quality");
            System.out.println("6. Add Location");
            System.out.println("7. Exit");
            System.out.print("Please enter your choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewWeatherByLongLat();
                    break;
                case 2:
                    viewWeatherByCityCountry();
                    break;
                case 3:
                    showCurrentWeather();
                    break;
                case 4:
                    showFiveDaysForecast();
                    break;
                case 5:
                    checkAirQuality();
                    break;
                case 6:
                    addLocation();
                    break;
                case 7:
                    if(storageChoice == 1){
                        saveUserDataToFile();
                    }
                    else{
                        saveUserDataToDatabase();
                    }
                    System.out.println("Thank you for using the Weather App. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    static void viewWeatherByLongLat() {
        System.out.println("Viewing weather by longitude and latitude...");
    }

    static void viewWeatherByCityCountry() {
        System.out.println("Viewing weather by city and country...");
    }

    static void showCurrentWeather() {
        System.out.println("Showing current weather...");
    }

    static void showFiveDaysForecast() {
        System.out.println("Showing 5 days forecast...");
    }

    static void checkAirQuality() {
        System.out.println("Checking air quality...");
    }

    static void addLocation() {
        System.out.println("Adding location...");
    }

    static void loadUserData() {
        try {
            File file = new File("user_data.txt");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                userCredentials.put(parts[0], parts[1]);
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    static void saveUserDataToFile() {
        // Save user data to text file
        try {
            FileWriter writer = new FileWriter("user_data.txt");
            for (String email : userCredentials.keySet()) {
                writer.write(email + "," + userCredentials.get(email) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving user data to file: " + e.getMessage());
        }
    }

    static void saveUserDataToDatabase() {
        // Save user data to SQLite database
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:user_data.db");
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM users"); // Clear existing data
            for (String email : userCredentials.keySet()) {
                String password = userCredentials.get(email);
                String query = "INSERT INTO users (email, password) VALUES ('" + email + "', '" + password + "')";
                statement.executeUpdate(query);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error saving user data to database: " + e.getMessage());
        }
    }
}
