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

package com.ecommerceapp.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ecommerceapp.R;

public class GridSpacingItemDecorationUtil extends RecyclerView.ItemDecoration {

    private int mItemOffset;
    private boolean mIsTablet;

    public GridSpacingItemDecorationUtil(int itemOffset, boolean isTablet) {
        mItemOffset = itemOffset;
        mIsTablet = isTablet;
    }

    public GridSpacingItemDecorationUtil(@NonNull Context context, @DimenRes int itemOffsetId, RecyclerView recyclerView, boolean isTablet) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId), isTablet);
        recyclerView.setClipToPadding(false);
        if (isTablet) {
            recyclerView.setPadding(context.getResources().getDimensionPixelSize(itemOffsetId),
                    context.getResources().getDimensionPixelSize(itemOffsetId),
                    context.getResources().getDimensionPixelSize(itemOffsetId),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_64dp));
        } else {
            recyclerView.setPadding(0, context.getResources().getDimensionPixelSize(itemOffsetId),
                    0, context.getResources().getDimensionPixelSize(R.dimen.padding_64dp));
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mIsTablet) {
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        } else {
            outRect.set(0, mItemOffset, 0, mItemOffset);
        }
    }
}
