package com.ecommerceapp.data_model;


public class VariationsList {

    int variationId, variationsize;
    String variationColor;
    long variationPrice;

    public VariationsList(int variationId, long variationPrice, String variationColor, int variationsize) {
        this.variationId = variationId;
        this.variationPrice = variationPrice;
        this.variationColor = variationColor;
        this.variationsize = variationsize;
    }

    public int getVariationId() {
        return variationId;
    }

    public int getVariationsize() {
        return variationsize;
    }

    public String getVariationColor() {
        return variationColor;
    }

    public long getVariationPrice() {
        return variationPrice;
    }
}
