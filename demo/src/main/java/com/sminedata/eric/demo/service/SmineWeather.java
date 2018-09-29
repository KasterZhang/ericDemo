package com.sminedata.eric.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

/**
 * SmineWeather
 */
// @Service
public class SmineWeather {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private String API_SECRET_KEY;
    private String API_UID;
    private String CITY_CODE;
    private String API_TS;
    private String API_TTL = "1800";
    private String LOCATION = "beijing";
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

    public String getAPI_UID() {
        return this.API_UID;
    }

    public void setAPI_UID(String API_UID) {
        this.API_UID = API_UID;
    }

    public String getAPI_SECRET_KEY() {
        return this.API_SECRET_KEY;
    }

    public void setAPI_SECRET_KEY(String API_SECRET_KEY) {
        this.API_SECRET_KEY = API_SECRET_KEY;
    }

    public String getCITY_CODE() {
        return this.CITY_CODE;
    }

    public void setCITY_CODE(String CITY_CODE) {
        this.CITY_CODE = CITY_CODE;
    }

    public String getAPI_TS() {
        return this.API_TS;
    }

    public void setAPI_TS(String API_TS) {
        this.API_TS = API_TS;
    }

    public String getAPI_TTL() {
        return this.API_TTL;
    }

    public void setAPI_TTL(String API_TTL) {
        this.API_TTL = API_TTL;
    }

    public String getLOCATION() {
        return this.LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getUNIT() {
        return this.UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getLANGUAGE() {
        return this.LANGUAGE;
    }

    public void setLANGUAGE(String LANGUAGE) {
        this.LANGUAGE = LANGUAGE;
    }

    public String getAPI_BASE_URL() {
        return this.API_BASE_URL;
    }

    public void setAPI_BASE_URL(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    /**
     * create signature
     * 
     * @param data
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateSignature(String data, String key) throws UnsupportedEncodingException {
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

    /**
     * eg:%1B->%1b 接口要小写的时候可以用来转换urlencode
     * 
     * @param str
     * @return
     */
    @Deprecated
    private String ToLowcase(String str) {
        for (int i = -1; i <= str.lastIndexOf("%"); ++i) {
            i = str.indexOf("%", i);
            String target = str.substring(i, i + 3);
            str = str.replace(target, target.toLowerCase());
        }
        return str;
    }

    private String generateGetNowWeatherURL(String sig) throws MalformedURLException {
        return API_BASE_URL + "?ts=" + API_TS + "&ttl=" + API_TTL + "&uid=" + API_UID + "&sig=" + sig + "&location="
                + LOCATION + "&language=" + LANGUAGE + "&unit=" + UNIT;
    }

    /**
     * https://www.seniverse.com/ API
     * 
     * @throws IOException
     */
    public SmineWeather() throws IOException {
        this.setAPI_SECRET_KEY("ngwzcgwpk8l4zgb1");
        this.setAPI_UID("UCA7687926");

        this.setLOCATION("ip");// 北京
        this.setAPI_TS(String.valueOf(System.currentTimeMillis()));
        this.setAPI_TTL("60");

        String sigParams = "ts=" + API_TS + "&ttl=" + API_TTL + "&uid=" + API_UID;
        String sig = generateSignature(sigParams, API_SECRET_KEY);
        this.setSIGNATURE(sig);
        this.setAPI_URL(new URL(generateGetNowWeatherURL(sig)));

    }

    /**
     * get now weather
     * 
     * @return
     * @throws IOException
     */
    public String getNowWeather() throws IOException {
        this.getAPI_URL();
        HttpURLConnection httpConn = (HttpURLConnection) API_URL.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        httpConn.disconnect();
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        SmineWeather sw = new SmineWeather();
        System.out.println(sw.getNowWeather());
    }
}