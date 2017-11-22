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

package com.ecommerceapp.data_model;

public class DrawerItem {

    String itemTitle;
    int itemId;


    // For Notifications, Messages, Friend Request
    public DrawerItem(int itemId , String itemTitle){
        super();
        this.itemId = itemId;
        this.itemTitle = itemTitle;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getItemId() {
        return itemId;
    }
}
