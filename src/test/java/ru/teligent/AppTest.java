package ru.teligent;

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
import ru.teligent.models.TemperatureInfo;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void controllersTest() throws Exception {
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
    }
}
