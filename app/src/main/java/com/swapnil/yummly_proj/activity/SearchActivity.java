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
import android.view.View;
import android.widget.Toast;

import com.swapnil.yummly_proj.R;
import com.swapnil.yummly_proj.adapter.RecipeAdapter;
import com.swapnil.yummly_proj.model.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity  {


    private Toolbar mTopToolbar;
    private ArrayList<recipe> mRecipeList = new ArrayList<>();
    private ProgressDialog pDialog;
    public recipe content;
    public static String url = "https://www.food2fork.com/api/search?key=e8e9de949409c0e70e3cd3104304d29d&q=shredded%20chicken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicky);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar_quicky);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        if(!isConnected(SearchActivity.this)) buildDialog(SearchActivity.this).show();
        else{

        }
        Toast.makeText(SearchActivity.this, "Search", Toast.LENGTH_SHORT).show();
        new GetRecipeQuicky().execute();
    }
    //method is called when when the menu button on the device is pressed
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //initiating the search operation an listening to the callbacks
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("query", query);
                searchView.clearFocus();
                searchView.setQuery("", false);
                SearchActivity.this.setTitle(query);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=recipe "+query));
                startActivity(intent);
                return true;
            }
      //method called everytime the query text changed by user
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        return true;

    }


    class GetRecipeQuicky extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // getting json array
                    JSONArray jsonArray = jsonObj.getJSONArray("recipes");


                    for (int i = 16; i < jsonArray.length(); i++) {
                        JSONObject recipeItem = jsonArray.getJSONObject(i);

                        String text = recipeItem.getString("title");
                        String imageUrl = recipeItem.getString("image_url");
                        int rating = recipeItem.getInt("social_rank");
                        String recipes = recipeItem.getString("f2f_url");
                        mRecipeList.add(new recipe(imageUrl, text, rating, recipes));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.dismiss();


            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quick);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            RecipeAdapter mAdapter = new RecipeAdapter(SearchActivity.this, mRecipeList);
            recyclerView.setAdapter(mAdapter);
        }

    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(SearchActivity.this,MainActivity.class));
        finish();

    }

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
