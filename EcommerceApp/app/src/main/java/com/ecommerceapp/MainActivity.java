package com.ecommerceapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ecommerceapp.interfaces.FragmentDrawerListener;
import com.ecommerceapp.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements FragmentDrawerListener{

    private FragmentDrawer drawerFragment;
    private Context mContext;
    Toolbar toolbar;
    private ProductsListFragment productsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //drawer layout settings
        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),toolbar);
        drawerFragment.setDrawerListener(MainActivity.this);

        productsListFragment = new ProductsListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, productsListFragment).commit();


        fetchData();
    }

    @Override
    public void onDrawerItemSelected(int categoryId, String categoryName) {


        Log.d(MainActivity.class.getSimpleName(), "onDrawerItemSelected called " + categoryId);
        productsListFragment.filterProducts(categoryId);
    }

    /**
     * Method to manage response for GET, POST, DELETE, and PUT Request.
     *
     */
    public void fetchData() {

        if (PreferencesUtils.getResponse(mContext, PreferencesUtils.CATEGORIES_PRODUCTS) == null) {

            String url = "https://stark-spire-93433.herokuapp.com/json";
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.d(MainActivity.class.getSimpleName(), "Coming inside onResponse : " + response);

                    if (response != null && !response.isEmpty()) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray categories = json.optJSONArray("categories");
                            JSONArray rankings = json.optJSONArray("rankings");

                            PreferencesUtils.updateResponse(mContext, PreferencesUtils.CATEGORIES_PRODUCTS, categories);
                            PreferencesUtils.updateResponse(mContext, PreferencesUtils.RANKINGS, rankings);

                            drawerFragment.updateDrawer();
                            productsListFragment.showProducts();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return null;
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                        volleyError = new VolleyError(new String(volleyError.networkResponse.data));
                    }
                    return volleyError;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(request, "json_obj_req");
        }
    }

}
