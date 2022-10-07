import java.net.*;
import java.io.*;

public class UploadServer {
    private static int count;
    private static ServerSocket serverSocket;
    public static void main(String[] args) throws IOException {
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