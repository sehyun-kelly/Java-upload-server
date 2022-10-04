import java.net.*;
import java.io.*;

public class UploadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            Socket clientSocket = serverSocket.accept();
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String message = dataInputStream.readUTF();
            System.out.println("Title: "+message);

            out.writeBytes("HTTP/1.1 200 OK\r\n");
            out.writeBytes("Content-Type: text/html\r\n\r\n");
            String htmlPage = "<!DOCTYPE html>" +
                    "<html><head><title>File Upload Form</title></head>" +
                    "<body><h1>Upload file</h1>" +
                    "<form method=\"POST\" action=\"upload\" " +
                    "enctype=\"multipart/form-data\">" +
                    "<input type=\"file\" name=\"fileName\"/><br/><br/>" +
                    "Caption: <input type=\"text\" name=\"caption\"<br/><br/>" +
                    "<br />" +
                    "Date: <input type=\"date\" name=\"date\"<br/><br/>" +
                    "<br />" +
                    "<input type=\"submit\" value=\"Submit\"/>" +
                    "</form>" +
                    "</body></html>";
            out.writeBytes(htmlPage);

            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 8080 or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        while (true) {
            new UploadServerThread(serverSocket.accept()).start();
        }
    }
}
