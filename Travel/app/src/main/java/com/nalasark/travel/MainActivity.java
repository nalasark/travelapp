package com.nalasark.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainactivity);

        final SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final HashSet<String> empty = new HashSet<String>();
        final String emptyString = new String();
        final String nameData = sharedPref.getString("Name",emptyString);
        final String budgetData = sharedPref.getString("Budget",emptyString);
        final String FinalData = sharedPref.getString("Final",emptyString);
        final String[] itinerary = FinalData.split(",");

        //BUTTON: create itinerary
        Button createnew = (Button) findViewById(R.id.createnew);
        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("Itinerary");
                editor.remove("Name");
                editor.remove("Budget");
                editor.remove("Hotel");
                editor.commit();
                Intent create = new Intent(MainActivity.this, create.class);
                create.putExtra("Origin","MainActivity");
                startActivity(create);
            }
        });

        final TextView name = (TextView)findViewById(R.id.Name);
        final TextView budget = (TextView)findViewById(R.id.Budget);

        name.setText(nameData);
        budget.setText(budgetData);
        final ListView listView = (ListView) findViewById(R.id.ListView);

        ArrayList<String> list_final = new ArrayList<String>();
        final ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_final);
        listView.setAdapter(listArrayAdapter);
        listArrayAdapter.addAll(itinerary);
        listArrayAdapter.setNotifyOnChange(true);

        //BUTTON: reset
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("Final");
                editor.commit();
                listArrayAdapter.clear();
                name.setText("");
                budget.setText("");
            }
        });

        //BUTTON: weather
        Button weather = (Button) findViewById(R.id.weather);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherapp = new Intent(MainActivity.this, Weather.class);
                startActivity(weatherapp);
            }
        });


    }
}

