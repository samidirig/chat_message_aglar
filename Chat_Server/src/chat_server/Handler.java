/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mahmu
 */
public class Handler implements Runnable{

    public static ArrayList<Handler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bfReader;
    private BufferedWriter bfWriter;
    private String clientKullaniciAdi;
    
    public Handler(Socket socket){
        try {
            this.socket = socket;
            this.bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bfWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            this.clientKullaniciAdi = bfReader.readLine();
            clientHandlers.add(this);
            broadcastMsg(clientKullaniciAdi.toUpperCase()+"ortama giriş yaptı");
        } catch (Exception e) {
            closeAll(socket, bfReader, bfWriter);
        }
    }
    
    @Override
    public void run(){
        String message;
        while (socket.isConnected()) {            
            try {
                message = bfReader.readLine();
                broadcastMsg(message);
            } catch (IOException ex) {
                closeAll(socket, bfReader, bfWriter);
                break;
            }
        }
    }
    
    public void broadcastMsg(String message){
        for (Handler ch : clientHandlers) {
            try {
                if(!ch.clientKullaniciAdi.equals(clientKullaniciAdi)){
                    ch.bfWriter.write(message);
                    ch.bfWriter.newLine();
                    ch.bfWriter.flush();
                }
            } catch (Exception e) {
                closeAll(socket, bfReader, bfWriter);
            }
        }
    }
    public void removeHandler(){
        clientHandlers.remove(this);
        broadcastMsg(clientKullaniciAdi.toUpperCase()+"ortamdan ayrıldı.");
    }
    public void closeAll(Socket socket, BufferedReader bfReader, BufferedWriter bfWriter) {
        
        removeHandler();
        try {
            if(bfReader != null){
                bfReader.close();
            }else if (bfWriter != null) {
                bfWriter.close();
            }else if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
