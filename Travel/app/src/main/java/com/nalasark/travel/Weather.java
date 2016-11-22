package com.nalasark.travel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class Weather extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;

    String datasetName = "24hrs_forecast";
    String keyref = "781CF461BB6606AD8B3CD4F75182BDE2F8ABE242EA96140B";


    public Weather() throws XmlPullParserException {
    }


    private class WeatherAsync extends AsyncTask<String, String, String> {

        //preloading
        protected  void onPreExecute() {

        }
        protected String doInBackground(String... params) {

            try {
                callWebAPI(datasetName,keyref);
                System.out.println("IM HERE");
//                System.out.println(temperature);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);

        try{
            new WeatherAsync().execute();
        }
        catch (Exception e){e.printStackTrace();}
    }


    private void callWebAPI(String datasetName, String keyref) throws Exception {

        // Step 1: Construct URL
        String url = "http://api.nea.gov.sg/api/WebAPI/?dataset=" + datasetName +
                "&keyref=" + keyref;
        // Step 2: Call API Url
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : "+ url);
        System.out.println("Response Code : "+ responseCode);
        // Step 3: Check the response status
        if(responseCode == 200) {
// Step 3a: If response status == 200
// print the received xml
            System.out.println(readStream(con.getInputStream()));

        } else {
// Step 3b: If response status != 200
// print the error received from server

            System.out.println("Error in accessing API - " +
                    readStream(con.getErrorStream()));
        } }

    // Read the responded result

    private XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
    private XmlPullParser myparser = xmlFactoryObject.newPullParser();  //extract data from xml


    //    private String temperature;
    private String readStream(InputStream inputStream) throws Exception {


        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        String htemperature = null;
        String ltemperature = null;
        String unit = null;
        String date = null;
        String forecast = null;
        String hhumidity = null;
        String lhumidity = null;
        String humunit = null;


        myparser.setInput(inputStream, null);

        int eventType = myparser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {

            String tag = myparser.getName();

            switch(eventType) {
                case XmlPullParser.START_TAG:
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        System.out.println("Start document");
                    } else if (eventType == XmlPullParser.START_TAG) {
                        System.out.println("Start tag " + myparser.getName());
                    } else if (eventType == XmlPullParser.END_TAG) {
                        System.out.println("End tag " + myparser.getName());
                    } else if (eventType == XmlPullParser.TEXT) {
                        System.out.println("Text " + myparser.getText());
                    }

                    if (tag.equals("temperature")) {
                        htemperature = myparser.getAttributeValue(null, "high");
                        ltemperature = myparser.getAttributeValue(null, "low");
                        unit = myparser.getAttributeValue(null, "unit");
                        System.out.println(htemperature);
                    }
                    if (tag.equals("forecastIssue")) {
                        date = myparser.getAttributeValue(null, "date");
                    }

                    if (tag.equals("forecast")) {
                        if (myparser.next() == XmlPullParser.TEXT) {
                            forecast = myparser.getText();
                            System.out.println("Text " + myparser.getText());
                        }
                    }

                    if (tag.equals("relativeHumidity")) {
                        hhumidity = myparser.getAttributeValue(null,"high");
                        lhumidity = myparser.getAttributeValue(null,"low");
                        humunit = myparser.getAttributeValue(null,"unit");
                    }

                    break;

                case XmlPullParser.END_TAG:
            }
            eventType = myparser.next();
        }
        System.out.println("End document");

        tv1.setText(tv1.getText() + "Date: " + date );

        tv2.setText(tv2.getText() +  "\nWeather Forecast: \n" + forecast);

        tv3.setText(tv3.getText() + "\nHighest Temperature: \n" + htemperature +" " + unit
                + "\nLowest Temperature: \n" + ltemperature + " " + unit + "\n\n\nHigh Relative Humidity: \n"
                + hhumidity + " "+ humunit + "\nLow Relative Humidity: \n" + lhumidity + " " + humunit);


        while((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }

}