package com.example.Quotes_app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ColorsSpinnerAdapter extends BaseAdapter {
    ArrayList<String> colors;
    Context context;
    LayoutInflater inflater;

    public ColorsSpinnerAdapter(Context context, ArrayList<String> colors) {
        this.colors = colors;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int position) {
        return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_color;
        View view = inflater.inflate(R.layout.item_sppiner, parent, false);
        tv_color = view.findViewById(R.id.tv_color);
        tv_color.setText(getItem(position).toString());
        tv_color.setTextColor(position == 0? Color.LTGRAY:Color.parseColor(getItem(position).toString()));
        return  view;
    }
}
