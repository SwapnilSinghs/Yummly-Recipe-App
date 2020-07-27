package com.swapnil.yummly_proj.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.swapnil.yummly_proj.R;
import com.swapnil.yummly_proj.adapter.RecipeAdapter;
import com.swapnil.yummly_proj.model.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PopularActivity extends AppCompatActivity {


    private ArrayList<recipe> mRecipeList = new ArrayList<>();
    private ProgressDialog pDialog;
    public recipe content;
    public static  String url = "https://www.food2fork.com/api/search?key=e8e9de949409c0e70e3cd3104304d29d&q=shredded%20chicken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the layout
        setContentView(R.layout.activity_popular);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_popular);
        //to set the action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });
        //checking connectivity
        if(!isConnected(PopularActivity.this)) buildDialog(PopularActivity.this).show();
        else{

        }
        //executing getRecipe method to get the recipe
        new GetRecipe().execute();

    }

    //creating background thread to load the url
    //Asynctask<param,publishProgress,result>
    class GetRecipe extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing progress dialog
            pDialog = new ProgressDialog(PopularActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //passing the url to load onbackground
            String jsonStr = sh.makeServiceCall(url);
            //checking the response from url
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //creating object
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // getting json array
                    JSONArray jsonArray = jsonObj.getJSONArray("recipes");


                    for (int i = 0; i < 15; i++) {
                        JSONObject recipeItem = jsonArray.getJSONObject(i);

                        String text = recipeItem.getString("title");
                        String imageUrl = recipeItem.getString("image_url");
                        int rating = recipeItem.getInt("social_rank");
                        String recipes = recipeItem.getString("f2f_url");
                        mRecipeList.add(new recipe(imageUrl, text, rating, recipes));
                    }
                } catch (final JSONException e) {
                    //to show error if parsing not done
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                //if json data not loaded
                Log.e(TAG, "Couldn't get json from server.");

            }

            return null;
        }

        //execution after the background process to show the result
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //to dismiss the progressDialog after the background process is completed
            pDialog.dismiss();


            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            //recyclerView views size is fixed
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PopularActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            RecipeAdapter mAdapter = new RecipeAdapter(PopularActivity.this, mRecipeList);
            recyclerView.setAdapter(mAdapter);
        }

    }


   //intent from popular activity to main activity on back press
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(PopularActivity.this,MainActivity.class));
        finish();

    }


    //method to check connectivity
    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile =cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile!=null && mobile.isConnectedOrConnecting())||(wifi!=null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    //displaying alert if connection error
    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("NO INTERNET CONNECTION");
        builder.setMessage("You need to turn on your Mobile Data or wifi to access this. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }

}

