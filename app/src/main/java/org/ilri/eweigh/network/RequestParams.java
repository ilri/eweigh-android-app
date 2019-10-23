package org.ilri.eweigh.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * RequestParams
 *
 * Build params to be used for posting data
 *
 * 1. Create URL-encoded query params
 * 2. Create JSON params for posting JSON
 *
 * */
public class RequestParams {
    private Map<String, String> params;

    public RequestParams(){
        this.params = new HashMap<>();
    }

    public void put(String key, String value){
        params.put(key, value);
    }

    public String getQueryParams(){
        StringBuilder str = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            str.append(param.getKey()).append('=').append(param.getValue());

            if (iterator.hasNext()) {
                str.append('&');
            }
        }

        return str.toString();
    }

    public Map<String, String> getPostParams(){
        return params;
    }

    /**
     *
     * Return params for JSON Posting
     *
     * */
    public JSONObject getPostJSONParams(){
        JSONObject jsonParams = new JSONObject();

        for(Map.Entry<String, String> param : params.entrySet()){
            try {
                jsonParams.put(param.getKey(), param.getValue());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonParams;
    }
}
