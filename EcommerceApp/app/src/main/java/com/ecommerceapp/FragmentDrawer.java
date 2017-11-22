package com.ecommerceapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecommerceapp.adapter.NavigationDrawerAdapter;
import com.ecommerceapp.data_model.DrawerItem;
import com.ecommerceapp.interfaces.FragmentDrawerListener;
import com.ecommerceapp.utils.PreferencesUtils;
import com.ecommerceapp.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends Fragment {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private ProgressBar mProgressBar;
    FragmentDrawerListener drawerListener;
    private View mLayoutMain;
    List<DrawerItem> dataList;
    private Context mContext;
    private JSONArray categoriesArray;
    private TextView mHeaderTitle;
    private View containerView;


    public FragmentDrawer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext =  getActivity();
        // Inflate the layout for this fragment
        mLayoutMain = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mProgressBar = (ProgressBar) mLayoutMain.findViewById(R.id.progressBar);
        mHeaderTitle = (TextView) mLayoutMain.findViewById(R.id.headerTitle);
        mHeaderTitle.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) mLayoutMain.findViewById(R.id.recyclerView);

        dataList = new ArrayList<>();
        adapter = new NavigationDrawerAdapter(mContext, dataList, new NavigationDrawerAdapter.OnDrawerItemClickListener() {
            @Override
            public void onDrawerItemClick(View view, int position) {

                Log.d(FragmentDrawer.class.getSimpleName(), "onDrawerItemClick called..");
                DrawerItem drawerItem = dataList.get(position);
                mDrawerLayout.closeDrawer(containerView);
                if(drawerListener != null) {
                    drawerListener.onDrawerItemSelected(drawerItem.getItemId(), drawerItem.getItemTitle());
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));

        updateDrawer();

        return mLayoutMain;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                makeOptionMenuInvalidate();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                makeOptionMenuInvalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    /***
     * Method to invalidate option menu when the frament is attached to the activity.
     */
    private void makeOptionMenuInvalidate() {
        if (isAdded() && getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    /**
     * Method to update navigation drawer when dashboard response is updated.
     */
    public void updateDrawer() {

        if (PreferencesUtils.getResponse(mContext, PreferencesUtils.CATEGORIES_PRODUCTS) != null) {
            try {
                categoriesArray = new JSONArray(PreferencesUtils.getResponse(mContext,  PreferencesUtils.CATEGORIES_PRODUCTS));
                addDataToList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            makeOptionMenuInvalidate();

        }
    }

    private void addDataToList() {

        if(categoriesArray != null && categoriesArray.length() != 0)  {

            for(int i = 0; i < categoriesArray.length(); i++ ) {

                JSONObject jsonObject = categoriesArray.optJSONObject(i);
                String categoryName = jsonObject.optString("name");
                int categoryId = jsonObject.optInt("id");
                dataList.add(new DrawerItem(categoryId, categoryName));
            }

            mProgressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

}
