package com.example.planets.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.planets.R;
import com.example.planets.adapter.PlanetAdapter;
import com.example.planets.controller.ApiController;
import com.example.planets.controller.PlanetDataController;
import com.example.planets.model.Planet;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    private Handler handler;


    private ArrayList<Planet> plants;
    private ListView listView;
    private Button btn_exit;
    private static PlanetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        listView = (ListView) findViewById(R.id.list);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        plants = new ArrayList<>();
        plants = PlanetDataController.getPlanets(MainActivity.this);


        if (plants != null && plants.size() > 0) {

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Previous data loading from database")
                    .setContentText("Do you want to sync now ?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();


                            apiDataDownloading();

                        }
                    })
                    .show();
        } else {


            apiDataDownloading();

        }


        adapter = new PlanetAdapter(plants, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Planet se_planet = plants.get(position);
                try {
                    popup(se_planet);

                } catch (Exception e) {
                    Log.d("pop_up_theme", e.toString());
                }


            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to exit form this planet")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                            }
                        })
                        .show();

            }
        });

    }

    private void apiDataDownloading() {

        if (isNetworkConnected()) {

            pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setCancelable(false);
            pDialog.show();

            new Thread(new Runnable() {


                @Override
                public void run() {


                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            pDialog.setTitleText("Downloading API Data...");
                        }
                    });

                    try {
                        JSONObject jsonObject = ApiController.getJSONObjectFromURL(ApiController.domain + "planets");

                        plants = Planet.getDataFromJsonArray(jsonObject.getJSONArray("results"));

                        if (plants.size() > 0) {
                            PlanetDataController.savePlanetsToDb(plants, MainActivity.this);
                        }


                    } catch (JSONException e) {
                        Log.e("ERROR-Json", e.toString());

                    } catch (Exception e) {
                        Log.e("ERROR-main", e.toString());
                    }


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismissWithAnimation();

                            adapter = new PlanetAdapter(plants, getApplicationContext());
                            listView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                        }
                    });
                }
            }).start();


        } else {


            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Internet Connection")
                    .setContentText("Please enable internet connection")
                    .show();

        }


    }

    private void popup(Planet se_planet) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(se_planet.getName() + "");


        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom_data, null);

        TextInputEditText txt_v_orbital_period = (TextInputEditText) customLayout.findViewById(R.id.text_orbital_period);
        TextInputEditText txt_v_gravity = (TextInputEditText) customLayout.findViewById(R.id.text_gravity);

        txt_v_orbital_period.setEnabled(false);
        txt_v_gravity.setEnabled(false);

        txt_v_orbital_period.setText(se_planet.getOrbital_period());
        txt_v_gravity.setText(se_planet.getGravity());

        builder.setView(customLayout);


        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_down_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_down_data) {

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Do you want to sync with server data ?")
                    .setContentText("")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                            apiDataDownloading();

                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);

    }


}