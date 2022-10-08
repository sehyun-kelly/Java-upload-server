import java.net.*;
import java.io.*;

public class UploadServerThread extends Thread {
    private final Socket socket;
    private final int connectionCount;

    public UploadServerThread(Socket socket, int count) {
        super("DirServerThread");
        this.socket = socket;
        this.connectionCount = count;
    }

    public void run() {
        Class<?> myClass;

        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpServletRequest req = new HttpServletRequest(in);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            if (req.getConnectionAgent().equals("Console")) {
                System.out.println("Client " + connectionCount + " from console");
                myClass = Class.forName("UploadServlet");
                HttpServlet httpServlet = (HttpServlet) myClass.getConstructor().newInstance();
                httpServlet.doPost(req, res);
                out.write(baos.toByteArray());
            } else if (req.getConnectionAgent().equals("Web")) {
                System.out.println("Client " + connectionCount + " from web");
                myClass = Class.forName("WebUploadServlet");
                HttpServlet httpServlet = (HttpServlet) myClass.getConstructor().newInstance();

                switch (req.getMethod()) {
                    case "GET" -> httpServlet.doGet(req, res);
                    case "POST" -> httpServlet.doPost(req, res);
                }

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