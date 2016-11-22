package com.nalasark.travel;

import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainactivity);

        //BUTTON: create itinerary
        Button createnew = (Button) findViewById(R.id.createnew);
        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = new Intent(MainActivity.this, create.class);
                create.putExtra("Origin","MainActivity");
                startActivity(create);
            }
        });

        //READ FILE
        StringBuilder text = new StringBuilder();
        File file = new File(getApplicationContext().getExternalFilesDir(null), "itineraries.txt");

        if (!file.exists()) { //if file does not exist
            try {
                System.out.println(file.getAbsolutePath());
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try { //file exists
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();

                //format file data
                String[] itin = text.toString().split("\n");
                final ArrayList items = new ArrayList();
                for (int i=0;i<itin.length;i++) {
                    items.add(itin[i]);
                }

                ArrayAdapter<Object> ad = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_checked, items);
                ListView list = (ListView)findViewById(R.id.list_itin);
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, indiv_itinerary.class);
                        startActivity(i);
                    }
                });
            }
            catch (IOException e) {
                String error="";
                error=e.getMessage();
            }

        }

    }
}

