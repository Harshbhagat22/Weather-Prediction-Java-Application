import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class WeatherApp {

    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static JButton fetchButton;

    private static String apiKey = "c967b7c0b124e3cbb22ec650af53e9a7";

    public static String fetchWeatherData(String city) {
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apiKey + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
                double temp = json.getJSONObject("main").getDouble("temp");
                double feelsLike = json.getJSONObject("main").getDouble("feels_like");
                int humidity = json.getJSONObject("main").getInt("humidity");

                return "Weather: " + weather + "\n"
                        + "Temperature: " + temp + "°C\n"
                        + "Feels like: " + feelsLike + "°C\n"
                        + "Humidity: " + humidity + "%";
            } else {
                return "Error: Unable to fetch weather. HTTP code: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());

        locationField = new JTextField(20);
        fetchButton = new JButton("Fetch Weather");
        weatherDisplay = new JTextArea(10, 30);
        weatherDisplay.setEditable(false);

        frame.add(new JLabel("Enter the city name: "));
        frame.add(locationField);
        frame.add(fetchButton);
        frame.add(new JScrollPane(weatherDisplay));

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = locationField.getText().trim();
                if (!city.isEmpty()) {
                    String weatherInfo = fetchWeatherData(city);
                    weatherDisplay.setText(weatherInfo);
                } else {
                    weatherDisplay.setText("Please enter a city name.");
                }
            }
        });

        frame.setVisible(true);
    }
}


// to compile :    javac -cp .;json-20210307.jar WeatherApp.java
// to Run :     java -cp .;json-20210307.jar WeatherApp