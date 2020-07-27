package com.swapnil.yummly_proj.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swapnil.yummly_proj.R;
import com.swapnil.yummly_proj.activity.CustomPicasso;
import com.swapnil.yummly_proj.model.recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<recipe> mRecipeList;

    //adapt the data present in the format of array to show it
    public RecipeAdapter(Context context, ArrayList<recipe> recipeList){
        mContext = context;
        mRecipeList = recipeList;
    }

    //create the views
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //layout inflater class returns object corrresponding to the views created in layout file
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_recipe,parent,false );
        return new MyViewHolder(v);
    }

    //bind the views in view holder
    @Override
    public void onBindViewHolder(RecipeAdapter.MyViewHolder holder, int position) {
        recipe currentRecipe = mRecipeList.get(position);
        String imageUrl = currentRecipe.getImageUrl();
        final String text = currentRecipe.getText();
        int rating = currentRecipe.getRating();
        final String recipes = currentRecipe.getRecipe();
        //set text
        holder.text.setText(text);
        //set text
        holder.rating.setText(" "+ rating);
        //to display the image
        CustomPicasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        //on click listener of chef hat
        holder.recipes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipes));
                        mContext.startActivity(intent);

            }
        });
       //on click listener of image
        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=recipe "+text));
                        mContext.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        //declaring the size of array
        return mRecipeList.size();
    }
  // hold the views
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView text;
        public TextView rating;
        public ImageView recipes;

        public MyViewHolder(View itemView) {
            super(itemView);
            //calling by id
            imageView = itemView.findViewById(R.id.image);
            text =  itemView.findViewById(R.id.text);
            rating = itemView.findViewById(R.id.rating);
            recipes =  itemView.findViewById(R.id.recipe_url);



        }
    }


}

