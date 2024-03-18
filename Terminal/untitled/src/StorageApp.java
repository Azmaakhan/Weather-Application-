import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class StorageApp extends JFrame {
    private JComboBox<String> storageOptions;
    private JPanel mainPanel, sidebarPanel, loginPanel, loggedInPanel, addLocationPanel, checkAirQualityPanel;
    private JTextField latitudeField, longitudeField;;
    private JTextField usernameField;
    private JTextField latitudeFieldAQ, longitudeFieldAQ;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, addLocationButton, checkAirQualityButton;
    private JLabel loggedInLabel;
    private String selectedStorage;

    public StorageApp() {
        setTitle("Storage App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Storage options dropdown
        String[] storageOptionsArray = {"Text File Storage", "SQLite Storage"};
        storageOptions = new JComboBox<>(storageOptionsArray);
        storageOptions.setSelectedIndex(0);
        selectedStorage = storageOptionsArray[0]; // Initialize selectedStorage
        JPanel topPanel = new JPanel();
        topPanel.add(storageOptions);
        add(topPanel, BorderLayout.NORTH);

        // Login/Register panel
        loginPanel = new JPanel(new GridLayout(3, 2));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        latitudeFieldAQ = new JTextField();
        longitudeFieldAQ = new JTextField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);


        addLocationPanel = new JPanel(new GridLayout(4, 2));
        latitudeField = new JTextField();
        longitudeField = new JTextField();
        addLocationButton = new JButton("Add Location");

        addLocationPanel.add(new JLabel("Latitude:"));
        addLocationPanel.add(latitudeField);
        addLocationPanel.add(new JLabel("Longitude:"));
        addLocationPanel.add(longitudeField);
        addLocationPanel.add(addLocationButton);

        checkAirQualityPanel = new JPanel(new GridLayout(3, 2));
        latitudeFieldAQ = new JTextField();
        longitudeFieldAQ = new JTextField();
        checkAirQualityButton = new JButton("Check Air Quality");

        checkAirQualityPanel.add(new JLabel("Latitude:"));
        checkAirQualityPanel.add(latitudeFieldAQ);
        checkAirQualityPanel.add(new JLabel("Longitude:"));
        checkAirQualityPanel.add(longitudeFieldAQ);
        checkAirQualityPanel.add(checkAirQualityButton);

        // Logged-in panel
        loggedInPanel = new JPanel(new BorderLayout());
        sidebarPanel = new JPanel(new GridLayout(6, 1));
        JButton[] sidebarButtons = {
                new JButton("View Weather by Long/Lat"),
                new JButton("View Weather by City/Country"),
                new JButton("Show Current Weather"),
                new JButton("Show 5 Days Forecast"),
                new JButton("Check Air Quality"),
                new JButton("Add Location")
        };
        for (JButton button : sidebarButtons) {
            sidebarPanel.add(button);
        }
        loggedInLabel = new JLabel("Logged in as: ");
        loggedInPanel.add(loggedInLabel, BorderLayout.NORTH);
        loggedInPanel.add(sidebarPanel, BorderLayout.WEST);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.add(loginPanel);

        addLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());
                addLocation(latitude, longitude);
            }
        });

        checkAirQualityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double latitude = Double.parseDouble(latitudeFieldAQ.getText());
                double longitude = Double.parseDouble(longitudeFieldAQ.getText());
                checkAirQuality(latitude, longitude);
            }
        });

        for (JButton button : sidebarButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionCommand = e.getActionCommand();
                    switch (actionCommand) {
                        case "Add Location":
                            mainPanel.removeAll();
                            mainPanel.add(addLocationPanel);
                            mainPanel.revalidate();
                            mainPanel.repaint();
                            break;
                        case "Check Air Quality":
                            mainPanel.removeAll();
                            mainPanel.add(checkAirQualityPanel);
                            mainPanel.revalidate();
                            mainPanel.repaint();
                            break;
                        default:
                            // Perform other actions based on the clicked button
                            break;
                    }
                }
            });
        }

        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        storageOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedStorage = (String) storageOptions.getSelectedItem();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (selectedStorage.equals("Text File Storage")) {
                    if (loginFromFile(username, password)) {
                        loggedInLabel.setText("Logged in as: " + username);
                        mainPanel.removeAll();
                        mainPanel.add(loggedInPanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.");
                    }
                } else if (selectedStorage.equals("SQLite Storage")) {
                    if (loginFromSQLite(username, password)) {
                        loggedInLabel.setText("Logged in as: " + username);
                        mainPanel.removeAll();
                        mainPanel.add(loggedInPanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.");
                    }
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (selectedStorage.equals("Text File Storage")) {
                    registerToFile(username, password);
                } else if (selectedStorage.equals("SQLite Storage")) {
                    registerToSQLite(username, password);
                }
            }
        });

        for (JButton button : sidebarButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionCommand = e.getActionCommand();
                    // Perform action based on the clicked button
                    JOptionPane.showMessageDialog(null, "Performing action: " + actionCommand);
                }
            });
        }
    }

    private boolean loginFromFile(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean loginFromSQLite(String username, String password) {
        String url = "jdbc:sqlite:user_data.db";
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void registerToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
            writer.flush();
            JOptionPane.showMessageDialog(null, "Registration successful!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void registerToSQLite(String username, String password) {
        String url = "jdbc:sqlite:user_data.db";
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registration successful!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addLocation(double latitude, double longitude) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=fb2e59cfc6024b27f5cd9bd935de4f5d");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String cityName = jsonObject.getString("name");
                String countryCode = jsonObject.getJSONObject("sys").getString("country");
                long timeZone = jsonObject.getLong("timezone");
                long sunriseTime = jsonObject.getJSONObject("sys").getLong("sunrise");
                long sunsetTime = jsonObject.getJSONObject("sys").getLong("sunset");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                double feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like");
                long fetchingTime = System.currentTimeMillis();

                // Save data
                saveWeatherData(cityName, countryCode, timeZone, sunriseTime, sunsetTime, description, feelsLike, fetchingTime);
                JOptionPane.showMessageDialog(null, "Location added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Error fetching weather data. Response code: " + responseCode);
            }
        } catch (IOException | JSONException ex) {
            JOptionPane.showMessageDialog(null, "Error adding location: " + ex.getMessage());
        }
    }

    private void saveWeatherData(String cityName, String countryCode, long timeZone, long sunriseTime, long sunsetTime, String description, double feelsLike, long fetchingTime) {
        if (selectedStorage.equals("Text File Storage")) {
            saveToFile(cityName, countryCode, timeZone, sunriseTime, sunsetTime, description, feelsLike, fetchingTime);
        } else if (selectedStorage.equals("SQLite Storage")) {
            saveToDatabase(cityName, countryCode, timeZone, sunriseTime, sunsetTime, description, feelsLike, fetchingTime);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid storage choice.");
        }
    }

    private void saveToFile(String cityName, String countryCode, long timeZone, long sunriseTime, long sunsetTime, String description, double feelsLike, long fetchingTime) {
        try (FileWriter writer = new FileWriter("weather_data.txt", true)) {
            writer.write(cityName + "," + countryCode + "," + timeZone + "," + sunriseTime + "," + sunsetTime + "," + description + "," + feelsLike + "," + fetchingTime + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving weather data to file: " + e.getMessage());
        }
    }

    private void saveToDatabase(String cityName, String countryCode, long timeZone, long sunriseTime, long sunsetTime, String description, double feelsLike, long fetchingTime) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:weather_data.db");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO weather_data (city_name, country_code, time_zone, sunrise_time, sunset_time, description, feels_like, fetching_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, countryCode);
            preparedStatement.setLong(3, timeZone);
            preparedStatement.setLong(4, sunriseTime);
            preparedStatement.setLong(5, sunsetTime);
            preparedStatement.setString(6, description);
            preparedStatement.setDouble(7, feelsLike);
            preparedStatement.setLong(8, fetchingTime);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving weather data to database: " + e.getMessage());
        }
    }

    private void checkAirQuality(double latitude, double longitude) {
        try {
            String apiKey = "7c1e8c40d3131a3931336096d8e2059f";
            String urlString = "http://api.openweathermap.org/data/2.5/air_pollution?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            BufferedReader reader;
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            // Read response content
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            // Parsing JSON response
            JSONObject jsonObject = new JSONObject(responseContent.toString());
            JSONArray list = jsonObject.getJSONArray("list");
            JSONObject airData = list.getJSONObject(0);
            JSONObject components = airData.getJSONObject("components");
            int aqi = airData.getJSONObject("main").getInt("aqi");

            // Displaying air quality information
            String message = "Air Quality Index (AQI): " + aqi + "\n";
            message += "Pollutant concentrations:\n";
            message += "Carbon Monoxide (CO): " + components.getDouble("co") + " µg/m³\n";
            message += "Nitrogen Monoxide (NO): " + components.getDouble("no") + " µg/m³\n";
            message += "Nitrogen Dioxide (NO2): " + components.getDouble("no2") + " µg/m³\n";
            message += "Ozone (O3): " + components.getDouble("o3") + " µg/m³\n";
            message += "Sulfur Dioxide (SO2): " + components.getDouble("so2") + " µg/m³\n";
            message += "Fine Particulate Matter (PM2.5): " + components.getDouble("pm2_5") + " µg/m³\n";
            message += "Coarse Particulate Matter (PM10): " + components.getDouble("pm10") + " µg/m³\n";
            message += "Ammonia (NH3): " + components.getDouble("nh3") + " µg/m³";

            JOptionPane.showMessageDialog(null, message);

        } catch (IOException | JSONException e) {
            JOptionPane.showMessageDialog(null, "Error checking air quality: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StorageApp().setVisible(true);
            }
        });
    }
}
