package com.sminedata.eric.demo.service;

import java.io.IOException;

import com.sminedata.eric.demo.controller.IWeatherService;

import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements IWeatherService {
    @Override
    public String showNowWeather() {
        SmineWeather sw;
        String result = null;
        try {
            sw = new SmineWeather();
            result = sw.getNowWeather();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}