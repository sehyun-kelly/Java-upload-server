import java.io.*;

public class HttpServletRequest {
    private InputStream inputStream = null;

    public HttpServletRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        StringBuilder request = new StringBuilder();
        while (inputStream.available() != 0) {
            request.append((char) inputStream.read());
        }
        System.out.println(request);
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}