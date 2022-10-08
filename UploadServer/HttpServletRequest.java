import java.io.*;

public class HttpServletRequest {
    private InputStream inputStream;
    private String requestMethod = null;
    private String requestPath = null;
    public HttpServletRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        StringBuilder request = new StringBuilder();
        while (inputStream.available() != 0) {
            request.append((char) inputStream.read());
        }
        System.out.println(request);

        parseMethod(request.toString());
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void parseMethod(String request) {
        String[] components = request.split(" ");
        requestMethod = components[0];
        if (components.length > 1) {
            requestPath = components[1];
        } else {
            requestPath = "/undefined";
        }
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public boolean checkValidRequest() {
        if (!requestMethod.equals("GET") && !requestMethod.equals("POST")) {
            return false;
        }
        return !requestPath.equals("/undefined");
    }
}