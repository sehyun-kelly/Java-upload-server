import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.htmlPage = "<!DOCTYPE html>\r\n" +
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Path currentRelativePath = Paths.get("");
        response.htmlPage = "<!DOCTYPE html>\r\n" +
                "<html>\n" +
                "   <head>\n" +
                "       <title>File Upload Succeed</title>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "       <p>Upload succeeded!</p><br/>\n" +
                "       <div><ul>" + getListing(currentRelativePath.toAbsolutePath().getParent() + "/images/")
                + "</ul></div>\n" +
                "       <form method=\"GET\" action=\"\">\n" +
                "           <input type=\"submit\" value=\"back\"/>\n" +
                "       </form>\n" +
                "   </body>\n" +
                "</html>";
    }

    private String getListing(String path) {
        File dir = new File(path);
        String[] chld = dir.list();

        if (chld == null) return "No files found";

        String dirList = "All images:";
        for (String s : chld) {
            dirList += "<li>" + s + "</li>";
        }
        return dirList;
    }

}