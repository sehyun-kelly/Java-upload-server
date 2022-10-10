package UploadServer;

import java.net.*;
import java.io.*;
import java.util.logging.FileHandler;

public class UploadServer {
    public static FileHandler fh;
    static {
        try {
            File f = new File("log.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            fh = new FileHandler("log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int count;
    private static ServerSocket serverSocket;
    public static void main(String[] args) {
        count = 0;
        System.out.println("starting server");
        try {
            serverSocket = new ServerSocket(8080);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                UploadServerThread uploadServerThread = new UploadServerThread(clientSocket, count);
                uploadServerThread.start();
                count++;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 8080 or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }
}