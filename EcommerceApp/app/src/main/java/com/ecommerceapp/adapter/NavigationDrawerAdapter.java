/*
 *   Copyright (c) 2016 BigStep Technologies Private Limited.
 *
 *   You may not use this file except in compliance with the
 *   SocialEngineAddOns License Agreement.
 *   You may obtain a copy of the License at:
 *   https://www.socialengineaddons.com/android-app-license
 *   The full copyright and license information is also mentioned
 *   in the LICENSE file that was distributed with this
 *   source code.
 */

package com.ecommerceapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecommerceapp.R;
import com.ecommerceapp.data_model.DrawerItem;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DrawerItem> data ;
    private LayoutInflater inflater;
    private Context mContext;
    private OnDrawerItemClickListener mOnDrawerItemClickListener;

    public NavigationDrawerAdapter(Context context, List<DrawerItem> data,
                                   OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mContext = context;
        this.data = data;
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        inflater = LayoutInflater.from(context);
    }

    public interface OnDrawerItemClickListener {
        void onDrawerItemClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new MyViewHolder(mContext, inflater.inflate(R.layout.drawer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

            final MyViewHolder holder = (MyViewHolder) viewHolder;
            DrawerItem current = data.get(position);
            //Setting the Main Header
            holder.itemTitle.setText(current.getItemTitle());
            holder.icon.setText("\uf054");
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnDrawerItemClickListener.onDrawerItemClick(view, position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView itemTitle, icon;

        public MyViewHolder(Context context, View itemView) {
            super(itemView);
            container = itemView;
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            icon = (TextView) itemView.findViewById(R.id.nextIcon);
            icon.setTypeface(Typeface.createFromAsset(context.getAssets(), "fontIcons/fontawesome-webfont.ttf"));
        }
    }
}
