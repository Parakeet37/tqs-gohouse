package com.mycompany.tqs.gohouse;

/**
 *
 * @author demo
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import javax.ejb.Singleton;

@Singleton
public class Requester{

    // essential URL structure is built using constants
    public static final String ACCESS_KEY = "266d51d786e0680392fa8a25a29a19d4";
    public static final String BASE_URL = "http://apilayer.net/api/";

    // this object is used for executing requests to the (REST) API
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    
    
    public List<String> getAllCurrencies(){
        
        return null;
    }
    
    

    public String calculateExchange(String fromValue, String fromCurrency, String toCurrency) {
        return null;
    }
}
