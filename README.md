# 🌦️ Weather Forecast API

This project provides a RESTful API for fetching weather forecasts. It's built with Spring Boot, designed to be robust and scalable.

---

## 📚 Table of Contents

- [Features](#features)  
- [Technologies Used](#technologies-used)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [API Keys Configuration](#api-keys-configuration)  
  - [Building the Project](#building-the-project)  
  - [Running the Application](#running-the-application)  
- [API Endpoints](#api-endpoints)  
- [Project Structure](#project-structure)  
- [Contributing](#contributing)  
- [License](#license)

---

## ✨ Features

- **Current Weather Retrieval:** Fetches current weather conditions for a specified location (e.g., city name, ZIP code).
- **Modular Design:** Dedicated controller and service layers.
- **Easy to Extend:** Prepared for future features like multi-day forecasts or IP-based weather lookups.

---

## 🛠️ Technologies Used

- **Spring Boot** – Core application framework
- **Java 17+**
- **Maven** – Build and dependency management
- *(Placeholder)*: Integrates with an external weather API (e.g., OpenWeatherMap, WeatherAPI.com, AccuWeather)
- *(Optional)*: Lombok, validation libraries, Spring Data JPA

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17 or higher  
- [Download JDK](https://adoptium.net/)
- Apache Maven 3.6.0+  
- [Download Maven](https://maven.apache.org/)
- IDE (optional): IntelliJ IDEA, VS Code, or Eclipse

---

### 🔑 API Keys Configuration

Obtain an API key from your weather provider and update this file:

`src/main/resources/application.properties`

```properties
IPSTACK_API_KEY=YOUR_EXTERNAL_IPSTACK_ACCESS_KEY
WEATHER_MAP_API_KEY=YOUR_EXTERNAL_OPENWEATHERMAP_APPID
