package ru.teligent.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Main App controller
 * @author Max Zhuravlov
 */
@Controller
@RequestMapping("")
public class HomeController {

    /**
     * Get Weather info
     * @return weather info object
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getWeatherInfo() {
        return "";
    }

    /**
     * Test method
     * @return "test" string to check controller health
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String getTest() {
        return "test";
    }
}
