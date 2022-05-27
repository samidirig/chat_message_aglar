/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;

/**
 *
 * @author mahmu
 */
public class ChatClient {

    /**
     * @param args the command line arguments
     */
    

    // gerekli olan parametre ve değişkenler;
    private static Socket socket;
    private static BufferedReader bfReader;
    private static BufferedWriter bfWriter;
    public static String kullaniciAdi;
    private static InetAddress ServerIp;
    private static String ip="127.0.0.1";
    public static int port=6000;
    public ChatClient(Socket socket, String kullaniciAdi){
        try {
            this.socket = socket;
            this.kullaniciAdi = kullaniciAdi;
            this.bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            closeAll(socket, bfReader, bfWriter);
        }
    }
    //mesajı bufferWriter'lar ile server üzerinden mesajları iletir
    public static void messageSend(){
        try {
            bfWriter.write(kullaniciAdi);
            bfWriter.newLine();
            bfWriter.flush();
            
            Scanner scanner = new Scanner(System.in);
            
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bfWriter.write(kullaniciAdi + " :" + message);
                bfWriter.newLine();
                bfWriter.flush();    
            }
            
        } catch (Exception e) {
            closeAll(socket, bfReader, bfWriter);
        }
        
    }
    //server üzerinden bufferReader'lar ile gelen mesajları alırız
    public static void waitMessage(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {                    
                    try {
                        message = bfReader.readLine(); 
                        System.out.println(message);
                    } catch (Exception e) {
                        closeAll(socket, bfReader, bfWriter);
                    }
                }
            }
            
        }).start();
    }
    //buffer'lar ve socket'i kapalı değilse kendimiz tarafından kapatırız
    public static void closeAll(Socket socket, BufferedReader bfReader, BufferedWriter bfWriter){
        try {
            if(bfReader != null){
                bfReader.close();
            }else if(bfWriter != null){
                bfWriter.close();
            }else if (socket !=null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);
        String kullaniciAdi;
        Socket socket;
        ChatClient client;
        
        System.out.print("Kullanici Adınızı giriniz: ");
        kullaniciAdi = scanner.nextLine();
        //socket ve client açarız ve mesajları bekleriz ve göndeririz
        socket = new Socket("localhost", port);
        client = new ChatClient(socket, kullaniciAdi);
        client.waitMessage();
        client.messageSend();
        
    }
    
}
