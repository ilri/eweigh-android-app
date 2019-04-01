package org.ilri.eweigh.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.ilri.eweigh.R;

import java.util.Map;

public class APIService {
    private static final String TAG = APIService.class.getSimpleName();

    private Context context;
    private ConnectivityInfo connDetector;

    public APIService(Context context){
        this.context = context;

        connDetector = new ConnectivityInfo(context);
    }

    /**
     *
     * Add request to queue. Check the availability of internet before making any requests
     * and notify user in case of any issue
     *
     * */
    private void addRequest(Request req){

        if (connDetector.hasInternetConnectivity()) {
            MySingleton.getInstance(context).addToRequestQueue(req);
        }
        else{
            Toast.makeText(context, context.getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * GET request
     *
     * */
    public void get(String endpoint, Response.Listener<String> listener,
                    Response.ErrorListener errorListener){
        Log.w(TAG, "VOLLEY_GET: " + endpoint);

        StringRequest req = new StringRequest(Request.Method.GET, endpoint, listener, errorListener);
        addRequest(req);
    }

    /**
     *
     * POST Request
     *
     * */
    public void post(String endpoint, final RequestParams params, Response.Listener<String> listener,
                     Response.ErrorListener errorListener){
        Log.w(TAG, "VOLLEY_POST: " + endpoint + " DATA: " + params.getPostJSONParams().toString());

        final StringRequest req = new StringRequest(Request.Method.POST, endpoint, listener, errorListener) {

            @Override
            protected Map<String, String> getParams() {
                return params.getPostParams();
            }
        };
        addRequest(req);
    }

    /**
     *
     * Post JSON
     *
     * */
    public void postJSON(String endpoint, final RequestParams params, Response.Listener<String> listener,
                         Response.ErrorListener errorListener){
        StringRequest req = new StringRequest(Request.Method.POST, endpoint, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.getPostJSONParams().toString().getBytes();
            }
        };
        addRequest(req);
    }

    /**
     *
     * File Uploads
     *
     * TODO: Consider MultipartRequest - https://stackoverflow.com/a/16803473/3310235
     *
     * */
    public void uploadFile(String endpoint, String base64EncodedFile){

    }

}
