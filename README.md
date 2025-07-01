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
---

## ✨ Features

- **Current Weather Retrieval:** Fetches current weather conditions of a location based on Ip Address.
- **Modular Design:** Dedicated controller and service layers.

---

## 🛠️ Technologies Used

- **Spring Boot** – Core application framework
- **Java 17+**
- **Maven** – Build and dependency management
- *(Placeholder)*: Integrates with an external weather API (e.g., OpenWeatherMap, IpStack)
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

Obtain an API key from your IpStack and OpenWeatherMap providers and store them in an .env file:

IPSTACK_API_KEY=YOUR_EXTERNAL_IPSTACK_ACCESS_KEY
WEATHER_MAP_API_KEY=YOUR_EXTERNAL_OPENWEATHERMAP_APPID

---

### Building the Project
1. Clone the repository:
```bash
git clone https://github.com/RanadeeshwarKamboji/weather-by-ip.git
```

2. Navigate to project directory:
```bash
cd weather-by-ip
```

3. Build the project:
```bash
# If using Maven
mvn clean install

# If using Gradle
./gradlew build
```
---

### Running the Application
1. Start the application:
```bash
# If using Maven
mvn spring-boot:run

# If using Gradle
./gradlew bootRun
```

2. The application will be available at:
```
http://localhost:8080
```

---

## API Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/weather_by_ip` | GET | Get weather by automatically detected IP |
| `/api/weather_by_ip?ip=INPUT_IP_ADDRESS` | GET | Get weather for specific IP address |

**Example Success Response**

{
    "ip": "2001:4860:4860::8888",
    "location": {
        "city": "Mountain View",
        "country": "United States"
    },
    "weather": {
        "temperature": "82.62°F",
        "humidity": 42,
        "description": "clear sky"
    }
}


**Example Error Response**

{
    "status": 422,
    "error": "Incorrect Details",
    "message": "Latitude and/or Longitude value is missing",
    "details": {
        "service": "Client Request Data",
        "issue": "Provided ip address failed validation or is missing.",
        "suggestion": "Please provide valid ip address"
    },
    "path": "/weather-by-ip",
    "timestamp": "2025-07-01T21:40:28.69883491"
}

---

## Project Structure
```
weather-by-ip/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/rana/weather_by_ip/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── models/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
├── README.md
└── DOCUMENTATION.md
```
