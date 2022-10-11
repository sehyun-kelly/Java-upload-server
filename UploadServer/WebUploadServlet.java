package UploadServer;

import HttpServlet.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static UploadServer.UploadServer.logger;

public class WebUploadServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.stringResponse = "<!DOCTYPE html>\r\n" +
                    "<html>\n" +
                    "   <head>\n" +
                    "       <title>File Upload Form</title>\n" +
                    "   </head>\n" +
                    "   <body>\n" +
                    "       <h1>Upload file</h1>\n" +
                    "       <form method=\"POST\" action=\"upload\" enctype=\"multipart/form-data\">\n" +
                    "           <input type=\"file\" name=\"fileName\"/><br/><br/>\n" +
                    "           Caption: <input type=\"text\" name=\"caption\"<br/><br/><br/>\n" +
                    "           Date: <input type=\"date\" name=\"date\"<br/><br/><br/>\n" +
                    "           <input type=\"submit\" value=\"Submit\"/>\n" +
                    "       </form>\n" +
                    "   </body>\n" +
                    "</html>";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Path currentRelativePath = Paths.get("");
        try {
            String fileName = currentRelativePath.toAbsolutePath() + "/images/" + request.getFileName();
            OutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(request.getFileArray());

            String describe = fileName + "&" + request.getCaption() + "@" + request.getDate() + "*";
            FileWriter fw = new FileWriter(currentRelativePath.toAbsolutePath() + "/images.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(describe);
            bw.newLine();
            bw.close();

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        response.stringResponse = "<!DOCTYPE html>\r\n" +
                "<html>\n" +
                "   <head>\n" +
                "       <title>File Upload Succeed</title>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "       <p>Upload succeeded!</p><br/>\n" +
                "       <div><ul>\n" + getListing(currentRelativePath.toAbsolutePath() + "/images/") +
                "       </ul></div>\n" +
                "       <form method=\"GET\" action=\"\">\n" +
                "           <input type=\"submit\" value=\"back\"/>\n" +
                "       </form>\n" +
                "   </body>\n" +
                "</html>";
    }

    private String getListing(String path) {
        File dir = new File(path);
        String[] child = dir.list();

        if (child == null) return "No files found";

        Arrays.sort(child);
        String dirList = "All images:";
        for (String s : child) {
            dirList += "<li>" + s + "</li>";
        }
        return dirList;
    }

}