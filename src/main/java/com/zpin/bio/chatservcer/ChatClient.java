package com.zpin.bio.chatservcer;

import java.io.*;
import java.net.Socket;

/**
 * @Author:zrt
 * @Date:2020/9/25-09-25-18:10
 * @Description:com.zpin.bio.chatservcer
 * @version:1.0
 */
public class ChatClient {

     private Socket socket;
     private BufferedReader reader;
     private BufferedWriter writer;
     private final String QUIT="quit";
     private final String ADDRESS="127.0.0.1";
     private final Integer PORT=7384;

     //发送消息给服务器
     public  void send(String msg) throws IOException {

         if(!socket.isOutputShutdown()){
             writer.write(msg+"\n");
             writer.flush();
         }
     }

     //从服务器接送消息
    public String receive() throws IOException {
        String msg=null;
        if(!socket.isOutputShutdown()){
            msg  = reader.readLine();
        }
        return msg;
    }

    //检查用户是否准备退出
    public boolean readyToQuit(String msg){
         return QUIT.equals(msg);
    }
    public void close(){
         if(writer!=null){
             try {
                 System.out.println("关闭socket");
                 writer.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
    }

    //初始化star
    public void start() {
        try {
            socket=new Socket(ADDRESS,PORT);
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //处理用户的输入

            //读取服务器转发的消息
            String msg=null;
            while((msg=reader.readLine())!=null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

    }

    public static void main(String[] args) {
        ChatClient chatClient=new ChatClient();
        chatClient.start();
    }

}
