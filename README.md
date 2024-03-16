# Weather Application Project

Welcome to our weather application project! In this project, we aim to design an intuitive weather application that provides users with accurate weather forecasts for any location. Utilizing various APIs provided by OpenWeatherMap, we'll gather weather data and present it in a user-friendly manner.

## Project Description

In this weather application where users can view weather forecasts for any location, We'll be utilizing the following APIs to obtain weather data in JSON format:

1. **Current Weather Data:** Obtain weather data for any location on Earth using longitude and latitude.
    - [API Link](https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key})

2. **5-day Forecast:** Get the weather forecast for a location for 5 days with a 3-hour interval.
    - [API Link](https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key})

3. **Air Pollution API:** Provides Air Quality Index and information about polluting gases.
    - [API Link](http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key})

These APIs are used from openweathermap.org.

## Project Structure

There're two parts of UI and Storage :

**UI:**
- Terminal-based UI (console application)
- JAVA desktop application

**Storage:**
- SQL-based storage
- ".txt" based storage mechanism

## Use Cases

Following Use Cases are configured & working currently:

1. Add multiple locations using longitude and latitude.
2. Add multiple locations using city/country name.
3. Display current weather conditions.
4. Show basic information like "Feels like," minimum and maximum temperature, etc.
5. Show sunrise and sunset time.
6. Display weather forecast for 5 days.
7. Add timestamp for weather records.
8. Implement Cache Management for improved performance.
9. Generate Notifications for poor weather conditions.
10. Show Air Pollution data.
11. Generate Notifications for poor air quality.
12. Display data about polluting gases.

## Stack

The Project is Implemented as follows :
- Java for the Terminal Part
- Java Swing for Desktop Application Part.

## HOW TO RUN IN TERMINAL
- Open any Java Compiler (This one is built in IntelliJ)
- One by one add all libraries in lib folder to build path
- Execute the Code
