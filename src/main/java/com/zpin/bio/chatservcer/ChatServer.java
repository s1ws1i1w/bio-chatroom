package com.zpin.bio.chatservcer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zrt
 * @Date:2020/9/23-09-23-16:33
 * @Description:com.zpin.bio.chatservcer
 * @version:1.0
 */
public class ChatServer {

      private Map<Integer, Writer> connctedClients;//用于存储
      private ServerSocket serverSocket;
      private final Integer PORT=8888;
      private final  String QUIT="quit";

      public ChatServer(){
          connctedClients=new HashMap<>();
      }

      public synchronized void addClient(Socket socket) throws IOException {
          if (socket != null) {
              int port = socket.getPort();
              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
              connctedClients.put(port, writer);
              System.out.println("客户端[" + port + "]已连接到服务器");
          }
      }

      public synchronized void removeClient(Socket socket) throws IOException {

          if(socket!=null) {
             int port=socket.getPort();
             if(connctedClients.containsKey(port)){
                 connctedClients.get(port).close();
          }connctedClients.remove(port);
             System.out.println("客户端["+port+"]已断开连接");
      }
      }

     public synchronized  void forwardMessage(Socket socket,String msg) throws IOException {

         for (Integer id : connctedClients.keySet()) {
             if(!id.equals(socket.getPort())){
                 Writer writer = connctedClients.get(id);
                 writer.write(msg);
                 writer.flush();

             }
         }
     }

     public void start(){
         try {
             serverSocket=new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             new Thread(new Handler(this,socket)).start();
         } catch (IOException e) {
             e.printStackTrace();
         }
         finally {
             close();
         }
     }

     public void close(){
          if(serverSocket!=null){
              try {
                  serverSocket.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
     }
     public boolean readyToQuit(String msg){
          if(msg.equals("QUIT")){
              return true;
          }
          return false;
     }


    public  static   void main(String[] args) {
          ChatServer chatServer=new ChatServer();
          chatServer.start();
    }



}
