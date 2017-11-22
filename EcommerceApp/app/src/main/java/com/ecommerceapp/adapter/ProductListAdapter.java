package com.ecommerceapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ecommerceapp.R;
import com.ecommerceapp.data_model.ProductList;
import com.ecommerceapp.data_model.VariationsList;
import com.ecommerceapp.interfaces.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ProductList> mItemList;
    private List<VariationsList> variationsList = new ArrayList<>();
    private VariationsAdapter variationsAdapter;

    public ProductListAdapter(Context context, List<ProductList> itemList){
        mItemList = itemList;
        mContext =  context;
    }

    public ProductListAdapter(Context context){
        mContext =  context;
    }

    public void update(List<ProductList> productsList){
        mItemList = productsList;
        notifyDataSetChanged();
    }

    public List<ProductList> getmItemList(){
        return mItemList;
    }


    public void filter(int categoryId) {
        Iterator<ProductList> it = mItemList.iterator();
        while (it.hasNext()) {
            if (it.next().getCategoryId() != categoryId)
                it.remove();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductListHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.products_list, parent, false), mContext);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ProductListHolder productViewHolder = (ProductListHolder) holder;
        productViewHolder.mainView.setTag("mainview");
        ProductList productInfo = mItemList.get(position);

        productViewHolder.productTitleView.setText(productInfo.getProductTitle());
        variationsList = new ArrayList<>();

        JSONArray productVariants = productInfo.getVariants();
        if(productVariants != null && productVariants.length() != 0) {

            for(int i = 0; i < productVariants.length(); i++) {
                JSONObject jsonObject = productVariants.optJSONObject(i);
                variationsList.add(new VariationsList(jsonObject.optInt("id"), jsonObject.optLong("price"),
                        jsonObject.optString("color"), jsonObject.optInt("size")));
            }

            variationsAdapter = new VariationsAdapter(mContext, variationsList, new OnItemClickListener() {

                @Override
                public void OnItemClick(int size, long price, String color) {

                    productViewHolder.productPrice.setVisibility(View.VISIBLE);
                    productViewHolder.productPrice.setText(mContext.getResources().getString(R.string.price_text) + price);
                    productViewHolder.productSize.setVisibility(View.VISIBLE);
                    productViewHolder.productSize.setText(mContext.getResources().getString(R.string.size_text) + size);
                }
            });
            productViewHolder.productVariationsRecyclerView.setAdapter(variationsAdapter);
            productViewHolder.productVariationsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            productViewHolder.productVariationsRecyclerView.setVisibility(View.GONE);
        }

    }

    public class ProductListHolder extends RecyclerView.ViewHolder{
        View mainView;
        TextView productTitleView, productPrice, productSize;
        RecyclerView productVariationsRecyclerView;
        ImageView productImage;

        public ProductListHolder(View itemView, Context context) {
            super(itemView);
            mainView = itemView;
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productTitleView = (TextView) itemView.findViewById(R.id.product_title);
            productSize = (TextView) itemView.findViewById(R.id.product_size);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productVariationsRecyclerView = (RecyclerView) itemView.findViewById(R.id.product_variations);
            productVariationsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
