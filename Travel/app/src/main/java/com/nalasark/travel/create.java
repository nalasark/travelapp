package com.nalasark.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class create extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create);

        final database data = new database();
        final EditText Name = (EditText) findViewById(R.id.Name);
        final Spinner hotel = (Spinner) findViewById(R.id.hotel);
        final EditText Budget = (EditText) findViewById(R.id.budget);
        final ListView listView = (ListView) findViewById(R.id.list);

        //reading writing
        final SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final Set<String> empty = new HashSet<String>();
        Set<String> itinerary = sharedPref.getStringSet("Itinerary",empty);

        ArrayList<String> list_hotels = new ArrayList<String>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_hotels);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hotel.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.addAll(data.getHotels());
        //String selection = hotel.getItemAtPosition(hotel.getSelectedItemPosition()).toString();

        ArrayList<String> list_places = new ArrayList<String>();
        final ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_places);
        listView.setAdapter(listArrayAdapter);
        listArrayAdapter.addAll(itinerary);
        listArrayAdapter.setNotifyOnChange(true);

        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> itinerary = sharedPref.getStringSet("Itinerary",empty);
                listArrayAdapter.clear();
                listArrayAdapter.addAll(itinerary);
                Toast.makeText(getBaseContext(),"refresh",Toast.LENGTH_SHORT).show();
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("Itinerary");
                editor.commit();
                listArrayAdapter.clear();
                Toast.makeText(getBaseContext(),"reset",Toast.LENGTH_SHORT).show();
            }
        });

        Button addplaces = (Button) findViewById(R.id.addplaces);
        addplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"add",Toast.LENGTH_SHORT).show();
                Intent places = new Intent(create.this, places.class);
                startActivity(places);
            }
        });

        Button createnew = (Button) findViewById(R.id.generate);
        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Name",Name.getText().toString());
                editor.putString("Hotel",hotel.getItemAtPosition(hotel.getSelectedItemPosition()).toString());
                editor.putString("Budget",Budget.getText().toString());
                editor.commit();

                Intent generate = new Intent(create.this, Itinerary.class);
                startActivity(generate);
            }
        });
    }
}
