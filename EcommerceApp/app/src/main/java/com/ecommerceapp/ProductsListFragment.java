package com.ecommerceapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.ecommerceapp.adapter.ProductListAdapter;
import com.ecommerceapp.data_model.ProductList;
import com.ecommerceapp.utils.GridSpacingItemDecorationUtil;
import com.ecommerceapp.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsListFragment extends Fragment implements View.OnClickListener{


    private View rootView;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mLayoutManager;
    private Context mContext;
    private ProductListAdapter mProductViewAdapter, productsFilterAdapter;
    private List<ProductList> mProductList;
    private JSONArray categoriesArray, rankingsArray;
    private LinearLayout sortingView;
    private Map<String, Boolean> mRadioButtonValueMap = new HashMap<>();

    public ProductsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mContext = getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        sortingView = (LinearLayout) getActivity().findViewById(R.id.sorting_view);
        sortingView.setOnClickListener(this);

        mProductList = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecorationUtil(mContext,
                R.dimen.padding_1dp, mRecyclerView, true));
        mProductViewAdapter = new ProductListAdapter(mContext, mProductList);
        productsFilterAdapter = new ProductListAdapter(mContext);
        mRecyclerView.setAdapter(mProductViewAdapter);

        showProducts();

        return rootView;
    }

    public void showProducts() {

        manageRankingArray();

        if (PreferencesUtils.getResponse(mContext, PreferencesUtils.CATEGORIES_PRODUCTS) != null) {
            try {
                categoriesArray = new JSONArray(PreferencesUtils.getResponse(mContext,  PreferencesUtils.CATEGORIES_PRODUCTS));
                addDataToList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void filterProducts (int categoryId) {
        productsFilterAdapter.update(new ArrayList<>(mProductViewAdapter.getmItemList()));
        productsFilterAdapter.filter(categoryId);
        mRecyclerView.setAdapter(productsFilterAdapter);
    }

    private void addDataToList() {

        JSONObject productsViewCountObject = rankingsArray.optJSONObject(0).optJSONObject("products");
        JSONObject productsOrderCountObject = rankingsArray.optJSONObject(1).optJSONObject("products");
        JSONObject productsShareCountObject = rankingsArray.optJSONObject(2).optJSONObject("products");

        for(int i = 0; i < categoriesArray.length(); i++) {

            JSONObject jsonObject = categoriesArray.optJSONObject(i);
            int categoryId = jsonObject.optInt("id");
            JSONArray products = jsonObject.optJSONArray("products");

            // For loop to fetch details of all products of a category
            for(int j = 0; j < products.length(); j++ ) {
                JSONObject productInfo = products.optJSONObject(j);
                int productId = productInfo.optInt("id");
                mProductList.add(new ProductList(productInfo.optString("name"), productId, categoryId,
                        productInfo.optJSONArray("variants"),
                        productsViewCountObject.optInt(String.valueOf(productId)),
                        productsOrderCountObject.optInt(String.valueOf(productId)),
                        productsShareCountObject.optInt(String.valueOf(productId))));
            }
        }

        mProductViewAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    public void manageRankingArray() {

        if (PreferencesUtils.getResponse(mContext, PreferencesUtils.RANKINGS) != null) {
            try {
                rankingsArray = new JSONArray(PreferencesUtils.getResponse(mContext,  PreferencesUtils.RANKINGS));

                String sortingKey;
                for(int i = 0; i < rankingsArray.length(); i++) {
                    JSONObject jsonObject = rankingsArray.optJSONObject(i);
                    JSONArray productsArray = jsonObject.optJSONArray("products");
                    sortingKey = "";
                    JSONObject newProductsArray = new JSONObject();

                    for(int j = 0; j < productsArray.length(); j++) {

                        JSONObject productObject = productsArray.optJSONObject(j);
                        JSONArray productsKeys = productObject.names();
                        sortingKey = productsKeys.optString(1);
                        newProductsArray.put(String.valueOf(productObject.optInt("id")), productObject.
                                optInt(productsKeys.optString(1)));
                    }
                    jsonObject.remove("products");
                    jsonObject.put("products", newProductsArray);
                    jsonObject.put("sorting_key", sortingKey);
                    rankingsArray.put(i, jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDialog() {

        final AppCompatDialog dialog = new AppCompatDialog(mContext);
        LinearLayout customFieldBlock = new LinearLayout(mContext);
        final RadioGroup radioGroup = new RadioGroup(mContext);
        radioGroup.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        radioGroup.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.padding_20dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_10dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_20dp));

        if (rankingsArray != null ){

            for(int i = 0; i < rankingsArray.length(); i++) {
                final AppCompatRadioButton radioButton = new AppCompatRadioButton(mContext);
                String rankingName = rankingsArray.optJSONObject(i).optString("ranking");
                final String sorting_key = rankingsArray.optJSONObject(i).optString("sorting_key");
                radioButton.setText(rankingName);
                radioButton.setTag(sorting_key);

                if(mRadioButtonValueMap.get(sorting_key) != null && mRadioButtonValueMap.get(sorting_key)) {
                    radioButton.setChecked(true);
                }

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        if (isChecked) {
                            dialog.dismiss();
                            mRadioButtonValueMap.clear();
                            mRadioButtonValueMap.put(compoundButton.getTag().toString(),true);

                            String sortingKey = (String) radioButton.getTag();
                            sortProducts(sortingKey);
                            mProductViewAdapter.notifyDataSetChanged();
                        }
                    }
                });

                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.body_small_font_size));
                radioGroup.addView(radioButton);
                radioButton.setGravity(GravityCompat.START| Gravity.CENTER_VERTICAL);

            }

            customFieldBlock.addView(radioGroup);
            dialog.setContentView(customFieldBlock);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sorting_view:
                showDialog();
                break;
        }
    }

    private void sortProducts(final String sortingKey) {

        Collections.sort(mProductList, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                ProductList product1 = (ProductList) o1;
                ProductList product2 = (ProductList) o2;

                switch (sortingKey) {

                    case "view_count":
                        return Integer.valueOf(product2.getViewCount()).compareTo(product1.getViewCount());

                    case "order_count":
                        return Integer.valueOf(product2.getOrderCount()).compareTo(product1.getOrderCount());

                    case "shares":
                        return Integer.valueOf(product2.getShareCount()).compareTo(product1.getShareCount());
                }

                return 0;
            }

        });
    }
}
