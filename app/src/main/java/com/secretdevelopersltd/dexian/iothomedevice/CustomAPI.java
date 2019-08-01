package com.secretdevelopersltd.dexian.iothomedevice;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.List;

public class CustomAPI {

    String TAG = "XIAN";

    String IP = "bdracingpigeon.bdpigeonweb.com";

    private void readOnlyFirst(Context context){
        String readURL = "http://"+IP+"/Pigeons/readOnlyFirst.php";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                readURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonRes = response.toString();

                        Log.i(TAG,jsonRes);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(jsonRes);// response will be the json String
                        //PigeonList pList = gson.fromJson(object, PigeonList.class);

                        //Log.i(TAG,pList.getRecords().toString());

                        //List<Pigeon> pp = pList.getRecords();

                        //PigeonList = pp;
                        //setAdapter();

                        //PB_loading.setVisibility(View.INVISIBLE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"REST API ERROR on READ ALL:"+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);


    }
    private void searchPigeon(Context context, String searchText){
        String searchURL = "http://"+IP+"/Pigeons/search.php?s="+searchText;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Log.i(TAG,"Search URL : "+searchURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                searchURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonRes = response.toString();

                        Log.i(TAG,jsonRes);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(jsonRes);// response will be the json String
                        /*PigeonList emp = gson.fromJson(object, PigeonList.class);

                        Log.i(TAG,emp.getRecords().toString());

                        List<Pigeon> pp = emp.getRecords();

                        PigeonList = pp;
                        setAdapter();
                        PB_loading.setVisibility(View.INVISIBLE);

                        Log.i(TAG,"pp size : "+pp.size());
                        Log.i(TAG,"pp 0 name = "+pp.get(0).getPigeonRingNumber());*/

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"REST API ERROR on Search:"+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
