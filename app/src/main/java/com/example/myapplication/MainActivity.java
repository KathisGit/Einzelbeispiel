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
    private Button btnBerechne;
    private TextView tv;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText) findViewById(R.id.Matrikelnummer);
        tv=(TextView) findViewById(R.id.ServerAntwort);
        btn=(Button) findViewById(R.id.button);
        btnBerechne=(Button) findViewById(R.id.btnBerechnen);

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

        btnBerechne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortNumbersBySize(et.getText().toString());
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

    //sort Numbers by Size without Prime Numbers
    public void sortNumbersBySize(String Matrikelnummer){
        //get char Array without Prime Numbers
        char[]StringToChar=deletePrime(Matrikelnummer).toCharArray();
        char temp;

        //Bubblesort Algorithmus
        for (int i=0;i<Matrikelnummer.length();i++){
            for (int j=0;j<Matrikelnummer.length()-1;j++){
                if (StringToChar[j]>StringToChar[j+1]){
                    temp=StringToChar[j];
                    StringToChar[j]=StringToChar[j+1];
                    StringToChar[j+1]=temp;
                }
            }
        }

        //set Text of Textview to sorted String
        tv.setText(new String(StringToChar));
    }

    //return String without Prime Numbers
    public String deletePrime(String Matrikelnummer){
        char[] charWithoutPrime=new char[Matrikelnummer.length()];
        int j=0;

        for (int i=0;i<Matrikelnummer.length();i++){
            if (!isPrim(Character.getNumericValue(Matrikelnummer.charAt(i)))){
                charWithoutPrime[j++]=Matrikelnummer.charAt(i);
            }
        }
        return new String(charWithoutPrime);
    }

    //check if Number is Prime
    public boolean isPrim(int a){
        //0,1 are not prime
        if(a==0||a==1)
            return false;
        //2 is Prime
        else if(a==2)
            return true;

        //if Number is divisable it is not prime
        else {

            for (int i = 2; i < a; i++) {
                if (a % i == 0)
                    return false;
            }
        }

        //else it is prime
        return true;
    }


    //check if Internet available
    public boolean internetAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        //true if network not null and connected or connecting to internet
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}