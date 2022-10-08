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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            if (socket.getLocalAddress().toString().contains("127")) {
                System.out.println("Client " + connectionCount + " from console");

                HttpServlet httpServlet = new UploadServlet();
                httpServlet.doPost(req, res);
                out.write(baos.toByteArray());
            } else {
                System.out.println("Client " + connectionCount + " from web");
                HttpServlet httpServlet = new WebUploadServlet();
                httpServlet.doGet(req, res);

                out.write(res.getResponse().toByteArray());
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