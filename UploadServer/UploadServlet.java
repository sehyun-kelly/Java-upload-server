package UploadServer;

import HttpServlet.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static UploadServer.UploadServer.logger;


public class UploadServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream baos = request.updateRequest(in);
            Path currentRelativePath = Paths.get("");
            String fileName = request.getCaption() + "_" + request.getDate() + "_" + request.getFileName();
            OutputStream outputStream = new FileOutputStream(currentRelativePath.toAbsolutePath() + "/images/" + fileName);
            baos.writeTo(outputStream);
            outputStream.close();

            File dir = new File(currentRelativePath.toAbsolutePath() + "/images/");
            String[] child = dir.list();
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            if (child != null) {
                Arrays.sort(child);
                for (String s : child) {
                    String[] content = s.split("_");
                    if (content.length >= 3) {
                        json.append("\t{\"Path\": \"").append(content[2]).append("\", ");
                        json.append("\"Caption\": \"").append(content[0]).append("\", ");
                        json.append("\"Date\": \"").append(content[1]).append("\"},\n");
                    }
                }
            }
            json.append("}\n");
            response.stringResponse = json.toString();
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}