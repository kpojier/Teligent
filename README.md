# Weather Sniffer 
Spring Boot standalone app implementing [OpenWeatherMap](http://api.openweathermap.org) API.

## Build & Run app
  * Download sources, unpack and navigate to app directory
  
  * Build by [Maven] (https://maven.apache.org) `mvn clean package`  
  
  * Run app `java -jar target/Weather-0.4.0.jar`
  
  You can download prebuilded jar file by <a href="https://dl.dropboxusercontent.com/u/10957282/Weather-0.4.0.jar" target="_blank">direct link</a>

## How to use
  * Open your browser with url `http:\\localhost:8080\`
  * For get weather forecast for your city you need add country code in [ISO 3166](https://en.wikipedia.org/wiki/ISO_3166) format and city name. 
  For example `http://localhost:8080/ru/Krasnodar` for Russian Federation city Krasnodar
  
## Response format
  Application return JSON document which contains city and country names, current temperature and the forecast minimum temperature for 3 days.
  
```{
    "country":"RU",
    "forecastTemp":-13.91,
    "city":"Krasnodar",
    "currentTemp":-2
}```  
  
## Change params
  All changed settings of the application are in the file app.properties
```
  main
   └── resources
          └── app.properties
```

### Cache params
  
  Application uses LRU cache. You can setup some cache params:
  
  * cache.size - Cache max size. By default cache size 50 forecasts
  * cache.live - Forecast live time in seconds. OpenWeatherMap update weather data every 3 hours so by default forecast cache live time is 10800 seconds
  
### Load data settings
  
  * loader.appid - OpenWeatherMap [API key (APPID)](http://openweathermap.org/appid). You can change this param if you want use your own application API key.
  * loader.units - Temperature units format. Temperature is available in Fahrenheit, Celsius and Kelvin units.
    - For temperature in Fahrenheit use units=imperial
    - For temperature in Celsius use units=metric
    - Temperature in Kelvin is used by default, no need to use units parameter in API call
