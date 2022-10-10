package HttpServlet;

import java.io.*;

public class HttpServletResponse {
    private ByteArrayOutputStream outputStream;

    public boolean available = false;
    public String stringResponse = null;

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
                    "Content-Length: " + stringResponse.getBytes().length + CRLF + CRLF
                    + stringResponse + CRLF + CRLF;
//            System.out.println(response);
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}