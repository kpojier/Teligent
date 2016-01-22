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
import ru.teligent.services.RestWeatherLoader;
import ru.teligent.services.WeatherLoader;

import java.time.LocalDateTime;
import java.util.Collections;
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
    @Autowired
    WeatherLoader weatherLoader;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(wac).build();
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

        // Load current weather & min forecast
        MvcResult weatherResult =  mockMvc.perform(get("/weather/"+COUNTRY_CODE+"/"+CITY_NAME+"/"))
                                                .andExpect(status().isOk())
                                                .andReturn();
        String weatherResultJson  = weatherResult.getResponse().getContentAsString();
        assertTrue(JsonPath.parse(weatherResultJson).read("$.city"   , String.class).equalsIgnoreCase(CITY_NAME));
        assertTrue(JsonPath.parse(weatherResultJson).read("$.country", String.class).equalsIgnoreCase(COUNTRY_CODE));
        assertTrue(JsonPath.parse(weatherResultJson).read("$.currentTemp", Double.class) > -50);
        assertTrue(JsonPath.parse(weatherResultJson).read("$.forecastTemp", Double.class) > -50);
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
        assertEquals(city.getName(), CITY_NAME);
        assertEquals(city.getId(), TEST_CITY_ID);

        // Weather Forecast model
        WeatherForecast forecast = new WeatherForecast();
        assertNotNull(forecast.getForecastsList());
        forecast.setCity(city);
        forecast.setForecastsList(Collections.singletonList(forecastItem));
        assertEquals(forecast.getCity(), city);
        assertEquals(forecast.getForecastsList().size(), 1);
        assertEquals(forecast.getForecastsList().get(0).getTimestamp(), TIMESTAMP);
    }

    @Test
    public void requestsTest() throws Exception {
        final String CITY_NAME    = "Moscow";
        final String COUNTRY_NAME = "ru";

        // Load current weather
        Weather weather = weatherLoader.loadCurrentWeather(CITY_NAME, COUNTRY_NAME);
        assertNotNull(weather);
        assertEquals(weather.getCityName(), CITY_NAME);
        assertTrue(weather.getTempInfo().getTemp() >= weather.getTempInfo().getMinTemp());
        assertTrue(weather.getTempInfo().getTemp() <= weather.getTempInfo().getMaxTemp());

        // Load weather forecast
        WeatherForecast forecast = weatherLoader.loadWeatherForecast(CITY_NAME, COUNTRY_NAME);
        assertNotNull(forecast);
        assertEquals(forecast.getCity().getName(), CITY_NAME);
        assertTrue(forecast.getForecastsList().size() > 0);
    }
}
