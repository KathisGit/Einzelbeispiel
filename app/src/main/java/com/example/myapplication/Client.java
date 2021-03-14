package com.example.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket;
    private String Matrikelnummer;
    private String answer;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;

    private static final String serverURLString="se2-isys.aau.at";
    private static final int serverPort=53212;



    @Override
    public void run() {
        try {

            initClient();
            sendMatrikelnummerToServer(Matrikelnummer);

            answer=receiveAnswerFromServer();

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initClient (){
        try {
            socket = new Socket(serverURLString,serverPort);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMatrikelnummerToServer(String Matrikelnummer) throws IOException {
        outputStream=new DataOutputStream(socket.getOutputStream());
        outputStream.writeBytes(Matrikelnummer+"\n");
        outputStream.flush();
    }

    public String receiveAnswerFromServer() throws IOException {
        socket.setSoTimeout(10000);
        inputStream=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return inputStream.readLine();
    }


    public void setMatrikelnummer(String Matrikelnummer){
        this.Matrikelnummer=Matrikelnummer;
    }


    public String getAnswer(){
        return answer;
    }

}
