package com.sminedata.eric.demo.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sminedata.eric.demo.controller.IWeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("weather")
public class WeatherResource {

    @Autowired
    private IWeatherService weatherService;

    @Path("now")
    @GET
    @Produces("text/plain;charset=utf-8")
    public String showWeather() {
        String result = this.weatherService.showNowWeather();
        return result;
    }
}