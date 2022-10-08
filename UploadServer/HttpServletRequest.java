import java.io.*;

public class HttpServletRequest {
    private InputStream inputStream = null;
    private String method;
    private int contentLength;
    private String contentType;
    private String userAgent;


    public HttpServletRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        StringBuilder request = new StringBuilder();
        while (inputStream.available() != 0) {
            request.append((char) inputStream.read());
        }
        System.out.println(request);
        parseRequest(request.toString());
    }

    private void parseRequest(String request){
        String[] stream = request.split("\n");
        System.out.println("stream[0]: " + stream[0]);
        if(stream[0].contains("GET")) method = "GET";
        else if(stream[0].contains("POST")) method = "POST";

        for(String line : stream){
            String[] parsedLine = line.split(": ");
//            if(parsedLine[0].equals("Content-Length")) contentLength = Integer.parseInt(parsedLine[1]);
            if(parsedLine[0].equals("User-Agent")) userAgent = parsedLine[1];
            if(parsedLine[0].equals("Content-Type")) contentType = parsedLine[1].split(";")[0];
        }

        System.out.println(method + ", " + contentType + ", " + contentLength + ", " + userAgent);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMethod() {
        System.out.println("this method is: " + this.method);
        return this.method; }
    public int getContentLength() { return this.contentLength; }
    public String getContentType() { return this.contentType; }
    public String getUserAgent() { return this.userAgent; }

    public boolean isFromWeb(){
        if(userAgent.contains("Mozilla")) return true;
        return false;
    }

}