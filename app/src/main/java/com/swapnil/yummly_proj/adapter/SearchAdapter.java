package com.swapnil.yummly_proj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.swapnil.yummly_proj.R;
import com.swapnil.yummly_proj.model.SearchRecipe;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private ArrayList<SearchRecipe> mArrayList;
    private ArrayList<SearchRecipe> mFilteredList;

    public SearchAdapter(ArrayList<SearchRecipe> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_title.setText(mFilteredList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<SearchRecipe> filteredList = new ArrayList<>();

                    for (SearchRecipe searchrecipe : mArrayList) {

                        if (searchrecipe.getTitle().toLowerCase().contains(charString) ) {

                            filteredList.add(searchrecipe);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<SearchRecipe>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView)view.findViewById(R.id.tv_title);

        }
    }

}
