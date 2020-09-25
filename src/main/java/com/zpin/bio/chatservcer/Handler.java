package com.zpin.bio.chatservcer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author:zrt
 * @Date:2020/9/25-09-25-17:26
 * @Description:com.zpin.bio.chatservcer
 * @version:1.0
 */
public class Handler implements Runnable {

    private ChatServer serverSocket;
    private Socket socket;

    public Handler(ChatServer serverSocket,Socket socket){
        this.serverSocket=serverSocket;
        this.socket=socket;
    }


    @Override
    public void run() {
        try {
            serverSocket.addClient(socket);
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg=null;
            while ((msg=reader.readLine())!=null){
                System.out.println("客户端["+socket.getPort()+"]:"+msg);
                String forwardmsg=msg+"\n";
                serverSocket.forwardMessage(socket,forwardmsg);
                //检查用户是否准备退出
                if(serverSocket.readyToQuit(msg)){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
