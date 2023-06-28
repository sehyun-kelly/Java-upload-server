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
                    "<style>\n" +
                    "            body {\n" +
                    "                font-family: Arial, sans-serif;\n" +
                    "                background-color: #f4f4f4;\n" +
                    "                padding: 20px;\n" +
                    "            }\n" +
                    "            h1 {\n" +
                    "                color: #333;\n" +
                    "            }\n" +
                    "            form {\n" +
                    "                background: #fff;\n" +
                    "                padding: 20px;\n" +
                    "                border-radius: 8px;\n" +
                    "                box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                    "            }\n" +
                    "            input[type=\"text\"], input[type=\"date\"], input[type=\"file\"] {\n" +
                    "                width: 95%;\n" +
                    "                padding: 10px;\n" +
                    "                border-radius: 4px;\n" +
                    "                border: 1px solid #ccc;\n" +
                    "                margin-bottom: 10px;\n" +
                    "            }\n" +
                    "            input[type=\"submit\"] {\n" +
                    "                background: #33c;\n" +
                    "                color: #fff;\n" +
                    "                border: none;\n" +
                    "                padding: 10px 20px;\n" +
                    "                border-radius: 4px;\n" +
                    "                cursor: pointer;\n" +
                    "            }\n" +
                    "            input[type=\"submit\"]:hover {\n" +
                    "                background: #339;\n" +
                    "            }\n" +
                    "        </style>"+
                    "   </head>\n" +
                    "   <body>\n" +
                    "       <h1>Upload file</h1>\n" +
                    "       <form method=\"POST\" action=\"upload\" enctype=\"multipart/form-data\">\n" +
                    "           <input type=\"file\" name=\"fileName\"/><br/><br/>\n" +
                    "           Caption: <br/><input type=\"text\" name=\"caption\"><br/><br/>\n" +
                    "           Date: <br/><input type=\"date\" name=\"date\"><br/><br/>\n" +
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
            String fileName = request.getCaption() + "_" + request.getDate() + "_" + request.getFileName();
            OutputStream outputStream = new FileOutputStream(currentRelativePath.toAbsolutePath() + "/images/" + fileName);
            outputStream.write(request.getFileArray());
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        response.stringResponse = "<!DOCTYPE html>\r\n" +
                "<html>\n" +
                "   <head>\n" +
                "       <title>File Upload Succeed</title>\n" +
                "<style>\n" +
                "            body {\n" +
                "                font-family: Arial, sans-serif;\n" +
                "                background-color: #f4f4f4;\n" +
                "                padding: 20px;\n" +
                "            }\n" +
                "            h1 {\n" +
                "                color: #4CAF50;\n" +
                "            }\n" +
                "            div {\n" +
                "                background: #fff;\n" +
                "                padding: 20px;\n" +
                "                border-radius: 8px;\n" +
                "                box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "                margin-top: 20px;\n" +
                "            }\n" +
                "            ul {\n" +
                "                list-style-type: none;\n" +
                "                padding: 0;\n" +
                "            }\n" +
                "            li {\n" +
                "                margin-bottom: 5px;\n"+
                "            }\n" +
                "            input[type=\"submit\"] {\n" +
                "                background: #33c;\n" +
                "                color: #fff;\n" +
                "                border: none;\n" +
                "                padding: 10px 20px;\n" +
                "                border-radius: 4px;\n" +
                "                cursor: pointer;\n" +
                "                margin-top: 20px;\n" +
                "            }\n" +
                "            input[type=\"submit\"]:hover {\n" +
                "                background: #339;\n" +
                "            }\n" +
                "        </style>\n"+
                "   </head>\n" +
                "   <body>\n" +
                "       <h1>Upload succeeded!</h1><br/>\n" +
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
        String dirList = "<h3>Images uploaded: </h3>";
        for (String s : child) {
            dirList += "<li>" + s + "</li>";
        }
        return dirList;
    }

}
