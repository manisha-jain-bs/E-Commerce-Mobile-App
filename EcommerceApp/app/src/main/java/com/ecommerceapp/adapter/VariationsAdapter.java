package com.ecommerceapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ecommerceapp.R;
import com.ecommerceapp.data_model.VariationsList;
import com.ecommerceapp.interfaces.OnItemClickListener;

import java.util.List;

public class VariationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<VariationsList> mVariationsList;
    private Context mContext;
    VariationsList variation;
    private OnItemClickListener mOnItemClickListener;


    public VariationsAdapter(Context context, List<VariationsList> photoList, OnItemClickListener onItemClickListener) {

        mContext = context;
        mVariationsList = photoList;
        this.mOnItemClickListener = onItemClickListener;
    }


    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.variation_item, viewGroup, false));
    }


    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        variation = mVariationsList.get(position);
        final ViewHolder holder = (ViewHolder) viewHolder;
        String variationColor;
        switch (variation.getVariationColor()) {

            case "Golden":
                variationColor = "#FAFAD2";
                break;

            case "Silver":
                variationColor = "#D3D3D3";
                break;

            case "Light Blue":
                variationColor = "#ADD8E6";
                break;

            case "Brown":
                variationColor = "#8B4513";
                break;

            default:
                variationColor = variation.getVariationColor();
        }
        holder.variationImage.setBackgroundColor(Color.parseColor(variationColor));
        holder.variationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VariationsList variationsList = mVariationsList.get(holder.getAdapterPosition());
                mOnItemClickListener.OnItemClick(variationsList.getVariationsize(),
                        variationsList.getVariationPrice(), variationsList.getVariationColor());
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mVariationsList.size();
    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton variationImage;
        private View container;

        public ViewHolder(View v) {
            super(v);

            container = v;
            variationImage = (ImageButton) v.findViewById(R.id.variationImage);
        }
    }

}