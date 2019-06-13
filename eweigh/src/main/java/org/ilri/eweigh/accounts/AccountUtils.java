package org.ilri.eweigh.accounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountUtils {
    private static final String TAG = AccountUtils.class.getSimpleName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private APIService apiService;

    public AccountUtils(Context context){
        pref = context.getSharedPreferences("ew_pref",
                Context.MODE_PRIVATE);
        editor = pref.edit();

        apiService = new APIService(context);
    }


    /**
     *
     * Account Utils
     *
     * */
    public void persistUser(JSONObject user) {
        Log.d("Persist Login: ", user.toString());

        try {
            editor.putInt("userId", user.getInt("id"));
            editor.putString("name", user.getString("name"));
            editor.putString("email", user.getString("email"));
            editor.putString("mobile", user.getString("mobile"));
            editor.putString("photo", user.getString("photo"));
            editor.putInt("groupId", user.getInt("groupid"));
            editor.putString("groupName",  user.getString("groupname"));

            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User getUserDetails(){
        User user = new User();

        user.setUserId(pref.getInt("userId", 0));
        user.setFullName(pref.getString("name", ""));
        user.setEmail(pref.getString("email", ""));
        user.setMobile(pref.getString("mobile", ""));
        user.setPhoto(pref.getString("photo", ""));
        user.setGroupId(pref.getInt("groupId", 0));
        user.setGroupName(pref.getString("groupName", ""));

        return user;
    }

    public boolean isLoggedIn(){
        User user = getUserDetails();
        return user.getUserId() != 0 && !TextUtils.isEmpty(user.getFullName()) &&
                !TextUtils.isEmpty(user.getEmail());
    }

    public void logout(){
        editor.remove("userId");
        editor.remove("name");
        editor.remove("email");
        editor.remove("mobile");
        editor.remove("photo");
        editor.remove("groupId");
        editor.remove("groupName");

        editor.commit();
    }

    public void sendFCMTokenToServer(final String token) {
        Log.d(TAG, "Token: " + token);

        RequestParams params = new RequestParams();
        params.put(User.FCM_TOKEN, token);

        apiService.post(URL.UpdateAccount, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
