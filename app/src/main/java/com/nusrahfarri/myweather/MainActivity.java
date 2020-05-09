package com.nusrahfarri.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button search;
    TextView result;

    public void searchForWeather(View view )  {
        EditText cityName = findViewById(R.id.cityName);
        TextView result = findViewById(R.id.result);

        String cName = cityName.getText().toString();
        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+cName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            //First we check if data is retrieved successfully or not
            Log.i("content", content);
            //JSON

            JSONObject root = new JSONObject(content);
            JSONArray weatherArray = root.getJSONArray("weather");
            JSONObject firstWeather = weatherArray.getJSONObject(0);

            String main = firstWeather.getString("main");
            String description = firstWeather.getString("description");

            JSONObject secondMain = root.getJSONObject("main");
            long temp = Math.round(secondMain.getDouble("temp"));

            Log.i("main", main);
            Log.i("description", description);


            String resultText = "Main : "+main+"\nDescription : "+description+"\nTemperature : "+temp+"C";
            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static class Weather extends AsyncTask <String,Void,String> {
        // First string means URL is in String , void means nothing, second String means return type will be String

        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be sent. It acts as an array.
            try {
                URL url = new URL(address[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();
                //Retrieve data from url
                InputStream in = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);

                //Retrieve data and return it as a string
                int data = isr.read();
                String content = "";
                char ch;
                while (data > 0){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                } return content;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
