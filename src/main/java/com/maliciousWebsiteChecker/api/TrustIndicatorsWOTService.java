/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maliciousWebsiteChecker.api;

import com.maliciousChecker.common.Common;
import java.io.IOException;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author santosh
 */
public class TrustIndicatorsWOTService {
    
    private static final String API_URL = "http://api.mywot.com/0.4/public_link_json2?hosts=";
    private static final String API_KEY = "430046dd4de37d913f6e3f4ee6173b96d0362c78";
    
    public int[] trustScores(String domain) {
        int[] trustScores = new int[2];
        try {

            Response response = Common.getOkHttpResponse(API_URL + domain + "/&callback=process&key=" + API_KEY);

            String responseString = response.body().string();
            if (responseString != null) {
                responseString = responseString.replace("process(", "");
                responseString = responseString.replace(")", "");
                JSONObject jsonObject = new JSONObject(responseString);
                if (jsonObject.getJSONObject(domain).has("0")) {
                    trustScores[0] = (int) jsonObject.getJSONObject(domain).getJSONArray("0").get(0);
                }
                if (jsonObject.getJSONObject(domain).has("4")) {
                    trustScores[1] = (int) jsonObject.getJSONObject(domain).getJSONArray("4").get(1);
                }
            }
        } catch (JSONException | IOException ex) {
            
            
        }
        return trustScores;
    }
    
}
