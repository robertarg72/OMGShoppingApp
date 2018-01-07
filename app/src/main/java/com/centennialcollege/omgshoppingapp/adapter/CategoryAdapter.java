package com.centennialcollege.omgshoppingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.centennialcollege.omgshoppingapp.ProductsActivity;
import com.centennialcollege.omgshoppingapp.R;
import com.centennialcollege.omgshoppingapp.model.Category;

import java.util.ArrayList;

import static com.centennialcollege.omgshoppingapp.utils.Utils.CATEGORY_PARAM;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Category> categories;
    private Context context;

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_image);
            textView = itemView.findViewById(R.id.category_title);
        }
    }

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        final Category currentCategory = categories.get(position);
        holder.imageView.setImageBitmap(currentCategory.getImage());
        holder.textView.setText(currentCategory.getName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, categories.get(position).getName(),Toast.LENGTH_SHORT).show();

                Intent next = new Intent( context, ProductsActivity.class);
                next.putExtra(CATEGORY_PARAM, categories.get(position).getName());
                context.startActivity(next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}
