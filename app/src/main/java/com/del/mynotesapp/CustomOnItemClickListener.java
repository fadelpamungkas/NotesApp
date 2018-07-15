package com.del.mynotesapp;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemOnItemClickCallback){
        this.position = position;
        this.onItemClickCallback = onItemOnItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v, position);
    }

    public interface OnItemClickCallback{
        void onItemClicked(View view, int position);
    }
}
