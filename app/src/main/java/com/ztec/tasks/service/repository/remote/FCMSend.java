package com.ztec.tasks.service.repository.remote;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private Context context;
    public FCMSend(Context context) {
        this.context = context;
    }
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "key=AAAAiPcq_-Q:APA91bGrzfsYUSCmiIlyH2hnAb7VEEpZ8LQkZyXvHVJdnDdufIPXNDBvAPnjLfxBRyJxO0197qL_BH-utwQ4Y4-NGoHEt8IlArTEYzUG7oIR258umC8-qFhCaVEe3BI_28Fvf7dTmYWs";

    public void pushNotification(String token, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this.context);

        try {
            JSONObject json = new JSONObject();
            json.put("to", token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                    @Override
                    public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-type", "application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException ex) {

        }
    }
}
