package UploadServer;

import java.net.*;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UploadServer {
    public static String IP_PART = "192.168";
    public static Logger logger;
    public static FileHandler fh;
    static {
        try {
            File f = new File("log.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            fh = new FileHandler("log.txt", true);
            logger = Logger.getLogger(UploadServer.class.getName());
            logger.addHandler(UploadServer.fh);
            SimpleFormatter formatter = new SimpleFormatter();
            UploadServer.fh.setFormatter(formatter);
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
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                UploadServerThread uploadServerThread = new UploadServerThread(clientSocket, count);
                uploadServerThread.start();
                count++;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 8888 or listening for a connection");
            logger.info(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

    }
}