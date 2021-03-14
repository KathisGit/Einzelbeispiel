package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private Button btn;
    private TextView tv;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText) findViewById(R.id.Matrikelnummer);
        tv=(TextView) findViewById(R.id.ServerAntwort);
        btn=(Button) findViewById(R.id.button);

        //EventListener to SendToServer-Button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if Internet is available
                if (internetAvailable()){
                    sendToServer(et.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet ist nicht verfügbar!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Send Message to Server
    public void sendToServer(String Matrikelnummer){

        //create a new Thread for TCP-Client
        client=new Client();
        Thread thread=new Thread(client);
        client.setMatrikelnummer(Matrikelnummer);

        thread.start();

        //rejoin Client- and Main-Thread
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        //set Text of Textview (Answer from Server)
        tv.setText(client.getAnswer());

    }


    //check if Internet available
    public boolean internetAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        //true if network not null and connected or connecting to internet
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}