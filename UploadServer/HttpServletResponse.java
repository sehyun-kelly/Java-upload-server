import java.io.*;

public class HttpServletResponse {
    private ByteArrayOutputStream outputStream;
    public String htmlPage;

    public HttpServletResponse(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public ByteArrayOutputStream getResponse() {
        try {
            final String CRLF = "\r\n";

            String response = "HTTP/1.1 200 OK" + CRLF +
                    "Content-Length: " + htmlPage.getBytes().length + CRLF + CRLF
                    + htmlPage + CRLF + CRLF;

            outputStream.write(response.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}