public class WebUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.htmlPage = "<!DOCTYPE html>" +
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("doPost in WebUploadServlet");
    }
}