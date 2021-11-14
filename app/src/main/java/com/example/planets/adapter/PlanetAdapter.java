package com.example.planets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planets.R;
import com.example.planets.model.Planet;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class PlanetAdapter extends ArrayAdapter<Planet>{

    private ArrayList<Planet> planets;
    Context mContext;


    private static class ViewHolder {
        TextView txtPlanetName;
        TextView txtPlanetClimate;
        ImageView imgInfo;
    }

    public PlanetAdapter(ArrayList<Planet> data, Context context) {
        super(context, R.layout.row_item, data);
        this.planets = data;
        this.mContext=context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Planet se_planet = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtPlanetName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txtPlanetClimate = (TextView) convertView.findViewById(R.id.txt_climate);
            viewHolder.imgInfo = (ImageView) convertView.findViewById(R.id.img_planet);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.txtPlanetName.setText(   "Planet Name :  "+se_planet.getName());
        viewHolder.txtPlanetClimate.setText("Climate           :  "+se_planet.getClimate());


        String  imagePath = se_planet.getImg_url();

        if (imagePath.equalsIgnoreCase("")) {


            Picasso.get().load(R.drawable.st_img_4).into( viewHolder.imgInfo);
        }else{
            Picasso.get().load(imagePath).into(viewHolder.imgInfo);
        }

        return convertView;
    }
}
