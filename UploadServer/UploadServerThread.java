import java.net.*;
import java.io.*;
import java.time.Clock;

public class UploadServerThread extends Thread {
    private Socket socket;
    private int connectionCount;

    public UploadServerThread(Socket socket, int count) {
        super("DirServerThread");
        this.socket = socket;
        this.connectionCount = count;
    }

    public void run() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpServletRequest req = new HttpServletRequest(in);
            OutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            if (socket.getLocalAddress().toString().contains("127")) {
                System.out.println("Client " + connectionCount + " from console");

                HttpServlet httpServlet = new UploadServlet();
                httpServlet.doPost(req, res);
                out.write(((ByteArrayOutputStream) baos).toByteArray());
            } else {
                System.out.println("Client " + connectionCount + " from web");
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

                final String CRLF = "\r\n";

                String response = "HTTP/1.1 200 OK" + CRLF +
                        "Content-Length: " + htmlPage.getBytes().length + CRLF + CRLF
                        + htmlPage + CRLF + CRLF;

                out.write(response.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}