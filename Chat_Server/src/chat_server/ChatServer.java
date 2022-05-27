/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mahmu
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    
    // gerekli olan parametre ve değişkenler;
    private final ServerSocket ss;
    private static int port = 6000 ;
    
    public ChatServer(ServerSocket ss){
        this.ss = ss;
    }
    
    public void serverBaslat(){
        try {
            while(!ss.isClosed()){
            Socket socket = ss.accept();
            System.out.println("Client dostumuz bağlandı.");
            Handler clientHandler = new Handler(socket);
            Thread thread = new Thread(clientHandler);
            
            thread.start();
        }
        } catch (Exception e) {
            closeSS();
        }
    }
    
    public void closeSS(){
        try {
            if(ss != null){
                ss.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        ServerSocket ss = new ServerSocket(port);
        ChatServer server = new ChatServer(ss);
        server.serverBaslat();
    }
    
}
