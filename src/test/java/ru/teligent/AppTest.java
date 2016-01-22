package ru.teligent;

import com.jayway.jsonpath.JsonPath;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import ru.teligent.core.Application;
import ru.teligent.models.*;
import ru.teligent.services.LRUCache;
import ru.teligent.services.RestWeatherLoader;
import ru.teligent.services.WeatherLoader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Max Zhuravlov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class AppTest {

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;
    private List<City> cities = new ArrayList<>();
    @Autowired
    WeatherLoader weatherLoader;

    @Before
    public void setUp() {
        cities.add(new City("Moscow"     , "ru"));
        cities.add(new City("Krasnodar"  , "ru"));
        cities.add(new City("Kaluga"     , "ru"));
        cities.add(new City("Stavropol"  , "ru"));
        cities.add(new City("Novosibirsk", "ru"));
        cities.add(new City("Vladivostok", "ru"));
        cities.add(new City("Amsterdam"  , "nl"));
        cities.add(new City("Chita"      , "ru"));
        cities.add(new City("Donetsk"    , "ua"));
        cities.add(new City("Minsk"      , "by"));
        cities.add(new City("London"     , "gb"));
        cities.add(new City("London"     , "us"));
        cities.add(new City("Amsterdam"  , "nl"));
        cities.add(new City("Moscow"     , "ru"));
        cities.add(new City("Krasnodar"  , "ru"));
        cities.add(new City("Kaluga"     , "ru"));
        cities.add(new City("Amsterdam"  , "nl"));
        cities.add(new City("Bari"       , "it"));
        cities.add(new City("Stavropol"  , "ru"));
        cities.add(new City("Novosibirsk", "ru"));
        cities.add(new City("Vladivostok", "ru"));
        this.mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void cacheTest() {

    }

    @Test
    public void controllersTest() throws Exception {

        final String CITY_NAME    = "Moscow";
        final String COUNTRY_CODE = "ru";

        // Check health
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",is("UP")));

        // Controller test method
        MvcResult result = mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "test");

        // Front controller method
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        for (City city:cities) {
            // Load current weather & min forecast
            MvcResult weatherResult =  mockMvc.perform(get("/"+city.getCountry()+"/"+city.getName()+"/"))
                    .andExpect(status().isOk())
                    .andReturn();
            String weatherResultJson  = weatherResult.getResponse().getContentAsString();
            assertTrue(JsonPath.parse(weatherResultJson).read("$.city"   , String.class).equalsIgnoreCase(city.getName()));
            assertTrue(JsonPath.parse(weatherResultJson).read("$.country", String.class).equalsIgnoreCase(city.getCountry()));
            assertTrue(JsonPath.parse(weatherResultJson).read("$.currentTemp", Double.class) > -50);
            assertTrue(JsonPath.parse(weatherResultJson).read("$.forecastTemp", Double.class) > -50);
        }
    }

    @Test
    public void modelTest() throws Exception {

        // Temperature
        Random r = new Random();
        final double MAX_TEMP_VALUE  = -50 + 100 * r.nextDouble();
        final double MIN_TEMP_VALUE  = -50 + (MAX_TEMP_VALUE + 50) * r.nextDouble();
        final double TEMP_VALUE = MIN_TEMP_VALUE + (MAX_TEMP_VALUE - MIN_TEMP_VALUE) * r.nextDouble();

        assertTrue(MAX_TEMP_VALUE >= MIN_TEMP_VALUE);
        assertTrue(TEMP_VALUE     >= MIN_TEMP_VALUE);
        assertTrue(TEMP_VALUE     <= MAX_TEMP_VALUE);

        TemperatureInfo temperature = new TemperatureInfo();
        temperature.setMaxTemp(MAX_TEMP_VALUE);
        temperature.setMinTemp(MIN_TEMP_VALUE);
        temperature.setTemp(TEMP_VALUE);
        assertEquals(TEMP_VALUE, temperature.getTemp(), 0);
        assertEquals(MAX_TEMP_VALUE, temperature.getMaxTemp(), 0);
        assertEquals(MIN_TEMP_VALUE, temperature.getMinTemp(), 0);

        // Weather model
        final String CITY_NAME = "Krasnodar";
        final String COUNTRY_NAME = "ru";
        Weather weather = new Weather();
        weather.setCityName(CITY_NAME);
        weather.setTempInfo(temperature);

        assertEquals(CITY_NAME, weather.getCityName());
        assertEquals(weather.getTempInfo().getTemp(), temperature.getTemp(), 0);
        assertEquals(weather.getTempInfo().getTemp(), TEMP_VALUE, 0);

        // Weather Forecast item
        long TIMESTAMP = System.currentTimeMillis();
        ForecastItem forecastItem = new ForecastItem();
        forecastItem.setTimestamp(TIMESTAMP);
        forecastItem.setTempInfo(temperature);
        assertEquals(forecastItem.getTimestamp(), TIMESTAMP);
        assertEquals(forecastItem.getTempInfo().getTemp(), temperature.getTemp(), 0);

        // City model
        long TEST_CITY_ID = 542420;
        City city = new City();
        city.setName(CITY_NAME);
        city.setId(TEST_CITY_ID);
        city.setCountry(COUNTRY_NAME);
        assertEquals(city.getName(), CITY_NAME);
        assertEquals(city.getId(), TEST_CITY_ID);
        assertEquals(city.getCountry(), COUNTRY_NAME);

        // Weather Forecast model
        WeatherForecast forecast = new WeatherForecast();
        assertNotNull(forecast.getForecastsList());
        forecast.setCity(city);
        forecast.setForecastsList(Collections.singletonList(forecastItem));
        assertEquals(forecast.getCity(), city);
        assertEquals(forecast.getForecastsList().size(), 1);
        assertEquals(forecast.getForecastsList().get(0).getTimestamp(), TIMESTAMP);

        // Weather response
        double CURR_TEMP = 12.0;
        double MIN_TEMP  = -10.2;
        WeatherResponse response = new WeatherResponse(
                CITY_NAME,
                COUNTRY_NAME,
                CURR_TEMP,
                MIN_TEMP
        );
        assertTrue(response.getCity().equalsIgnoreCase(CITY_NAME));
        assertTrue(response.getCountry().equalsIgnoreCase(COUNTRY_NAME));
        assertEquals(CURR_TEMP, response.getCurrentTemp(), 0);
        assertEquals(MIN_TEMP, response.getForecastTemp(), 0);
        assertTrue(response.getTimestamp() <= System.currentTimeMillis());

    }

    @Test
    public void requestsTest() throws Exception {
        for(City city:cities) {

            // Load current weather
            Weather weather = weatherLoader.loadCurrentWeather(city.getName(), city.getCountry());
            assertNotNull(weather);
            assertTrue(weather.getCityName().equalsIgnoreCase(city.getName()));
            assertTrue(weather.getSysInfo().getCountry().equalsIgnoreCase(city.getCountry()));
            assertTrue(weather.getTempInfo().getTemp() >= weather.getTempInfo().getMinTemp());
            assertTrue(weather.getTempInfo().getTemp() <= weather.getTempInfo().getMaxTemp());

            // Load weather forecast
            WeatherForecast forecast = weatherLoader.loadWeatherForecast(city.getName(), city.getCountry());
            assertNotNull(forecast);
            assertTrue(forecast.getCity().getName().equalsIgnoreCase(city.getName()));
            assertTrue(forecast.getCity().getCountry().equalsIgnoreCase(city.getCountry()));
            assertTrue(forecast.getForecastsList().size() > 0);
        }
    }
}
