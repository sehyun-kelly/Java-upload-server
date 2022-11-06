package UploadServer;

import CustomException.InvalidConnection;
import HttpServlet.*;

import java.net.*;
import java.io.*;

import static UploadServer.UploadServer.logger;

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
            System.out.println("Client " + connectionCount);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpServletRequest req = new HttpServletRequest(in);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            if (!req.getConnectionAgent().equals("Web")) {
                System.out.println("Console connection");
                if (socket.getLocalAddress().toString().contains(UploadServer.IP_PART)) { // IP
                    myClass = Class.forName("UploadServer.UploadServlet");
                    HttpServlet httpServlet = (HttpServlet) myClass.getConstructor().newInstance();
                    httpServlet.doPost(req, res);
                    out.write(res.getResponse().toByteArray());
                } else {
                    throw new InvalidConnection();
                }
            } else {
                System.out.println("Web connection");
                myClass = Class.forName("UploadServer.WebUploadServlet");
                HttpServlet httpServlet = (HttpServlet) myClass.getConstructor().newInstance();

                if (req.getMethod() != null) {
                    switch (req.getMethod()) {
                        case "GET" -> httpServlet.doGet(req, res);
                        case "POST" -> httpServlet.doPost(req, res);
                    }

                    out.write(res.getResponse().toByteArray());
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}