package com.hyeok.m16_chat_pt.CustomView;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by GwonHyeok on 2014. 6. 12..
 */
public class SpinnerArrayAdapter<String> extends ArrayAdapter {
    private Context context;
    private String[] items;

    public SpinnerArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView text = (TextView) view.findViewById(android.R.id.text1);
//        text.setBackgroundColor(0xff00ff00); 스피너 눌렀을시 색상 변경.
        return view;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView itemtv = (TextView)convertView.findViewById(android.R.id.text1);
        itemtv.setText((CharSequence)items[position]);
        itemtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//        itemtv.setBackgroundColor(0xff00ff00); 스피너 메인 색상.
        return convertView;
    }
}
