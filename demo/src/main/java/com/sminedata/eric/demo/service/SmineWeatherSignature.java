package com.sminedata.eric.demo.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.sminedata.eric.demo.controller.IWeatherService;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class SmineWeatherSignature {

    private String TIANQI_DAILY_WEATHER_URL = "https://api.seniverse.com/v3/weather/daily.json";
    private String TIANQI_API_SECRET_KEY = "ngwzcgwpk8l4zgb1"; //
    private String TIANQI_API_USER_ID = "UCA7687926"; //
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private String API_SECRET_KEY;
    private String API_UID;
    private String CITY_CODE;
    private String API_TS = String.valueOf(System.currentTimeMillis());
    private String API_TTL = "60";
    private String LOCATION = "ip";
    private String UNIT = "c";
    private String LANGUAGE = "zh-Hans";
    private String API_BASE_URL = "https://api.seniverse.com/v3/weather/now.json";
    private URL API_URL;
    private String SIGNATURE;

    public URL getAPI_URL() {
        return this.API_URL;
    }

    public void setAPI_URL(URL API_URL) {
        this.API_URL = API_URL;
    }

    public String getSIGNATURE() {
        return this.SIGNATURE;
    }

    public void setSIGNATURE(String SIGNATURE) {
        this.SIGNATURE = SIGNATURE;
    }

    public SmineWeatherSignature() {
        String sigParams = "ts=" + API_TS + "&ttl=" + API_TTL + "&uid=" + API_UID;
        String sig = null;
        try {
            sig = this.createSignature(sigParams, API_SECRET_KEY);
            this.setSIGNATURE(sig);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            this.setAPI_URL(new URL(generateGetNowWeatherURL(sig)));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * create signature
     * 
     * @param data
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private String createSignature(String data, String key) throws UnsupportedEncodingException {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64String(rawHmac);
            // return Hex.encodeHexString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return URLEncoder.encode(result, "UTF-8");
    }

    @Deprecated
    private String ToLowcase(String str) {
        for (int i = -1; i <= str.lastIndexOf("%"); ++i) {
            i = str.indexOf("%", i);
            String target = str.substring(i, i + 3);
            str = str.replace(target, target.toLowerCase());
        }
        return str;
    }

    /**
     * create nowWeather URL
     * 
     * @param sig
     * @return
     * @throws MalformedURLException
     */
    private String generateGetNowWeatherURL(String sig) throws MalformedURLException {
        return API_BASE_URL + "?ts=" + API_TS + "&ttl=" + API_TTL + "&uid=" + API_UID + "&sig=" + sig + "&location="
                + LOCATION + "&language=" + LANGUAGE + "&unit=" + UNIT;
    }

    /**
     * Generate HmacSHA1 signature with given data string and key
     * 
     * @param data
     * @param key
     * @return
     * @throws SignatureException
     */
    private String generateSignature(String data, String key) throws SignatureException {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result = Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * Generate the URL to get diary weather
     * 
     * @param location
     * @param language
     * @param unit
     * @param start
     * @param days
     * @return
     */
    public String generateGetDiaryWeatherURL(String location, String language, String unit, String start, String days)
            throws SignatureException, UnsupportedEncodingException {
        String timestamp = String.valueOf(new Date().getTime());
        String params = "ts=" + timestamp + "&ttl=30&uid=" + TIANQI_API_USER_ID;
        String signature = URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
        return TIANQI_DAILY_WEATHER_URL + "?" + params + "&sig=" + signature + "&location=" + location + "&language="
                + language + "&unit=" + unit + "&start=" + start + "&days=" + days;
    }

    public static void main(String args[]) throws IOException {
        SmineWeatherSignature demo = new SmineWeatherSignature();
        // new SmineWeather();
        try {
            String url = demo.generateGetDiaryWeatherURL("beijing", "zh-Hans", "c", "1", "1");
            System.out.println("TEST URL:" + url);
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }

    }
}