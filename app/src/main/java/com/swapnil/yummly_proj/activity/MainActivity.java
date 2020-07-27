package com.swapnil.yummly_proj.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.swapnil.yummly_proj.R;
import com.swapnil.yummly_proj.adapter.RecipeAdapter;
import com.swapnil.yummly_proj.model.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity  {


    private ArrayList<recipe> mRecipeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipeAdapter mAdapter;
    private ProgressDialog pDialog;
    public recipe content;
    public static  String url = "https://www.food2fork.com/api/search?key=e8e9de949409c0e70e3cd3104304d29d&q=chicken%20breast&page=2";
            //drawer and action bar
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the layout
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);
        //checking connectivity
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else{

        }

        //executing getRecipe method to get the recipe
        new GetRecipe().execute();

        //initializing the instances declared in the drawer
        initInstances();
    }

   //creating background thread to load the url
    //Asynctask<param,publishProgress,result>
    class GetRecipe extends AsyncTask<Void,Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
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
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // getting json array
                    JSONArray jsonArray = jsonObj.getJSONArray("recipes");


                    for (int i = 3; i < jsonArray.length(); i++) {
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

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
            //recyclerView views size is fixed
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            RecipeAdapter mAdapter = new RecipeAdapter(MainActivity.this, mRecipeList);
            recyclerView.setAdapter(mAdapter);
        }

    }

    private void initInstances() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //intent created in drawer to go to the other activity
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        Intent intent_home = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent_home);
                        return true;
                    case R.id.nav_feedback:
                        Intent intent_feedback = new Intent(MainActivity.this, FeedbackActivity.class);
                        startActivity(intent_feedback);
                        return true;
                    case R.id.nav_popular:
                        Intent intent_popular= new Intent(MainActivity.this, PopularActivity.class);
                        startActivity(intent_popular);
                        return true;
                    case R.id.nav_search:
                        Intent intent_quicky= new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent_quicky);
                        return true;

                    case R.id.nav_contact:
                        Intent intent_contact = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent_contact);
                        return true;


                }
                return true;
            }
        });

    }






    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //return true the click event will be consumed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //method to exit on pressing back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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