package com.example.planets.model;

import android.util.Log;

import com.example.planets.controller.ApiController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Planet {
    private int p_id;
    private String name;
    private String climate;
    private String orbital_period;
    private String gravity;
    private String img_url;

    public Planet() {

    }

    public Planet(int p_id, String name, String climate, String orbital_period, String gravity, String img_url) {
        this.p_id = p_id;
        this.name = name;
        this.climate = climate;
        this.orbital_period = orbital_period;
        this.gravity = gravity;
        this.img_url = img_url;
    }


    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getOrbital_period() {
        return orbital_period;
    }

    public void setOrbital_period(String orbital_period) {
        this.orbital_period = orbital_period;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public static ArrayList<Planet> getDataFromJsonArray(JSONArray jsonArray) {
        ArrayList<Planet> planets = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                Planet planet = new Planet();

                planet.setP_id(i);
                planet.setName(object.getString("name"));
                planet.setOrbital_period(object.getString("orbital_period"));
                planet.setClimate(object.getString("climate"));
                planet.setGravity(object.getString("gravity"));

                String random_img_url = ApiController.img_container +i;
                planet.setImg_url(random_img_url);

                planets.add(planet);
            }

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        return planets;
    }

    @Override
    public String toString() {
        return  name;
    }
}
