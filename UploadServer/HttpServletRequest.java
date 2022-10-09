import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class HttpServletRequest {
    private InputStream inputStream = null;
    private String method;
    private String contentType;
    private String userAgent;
    private byte[] fileArray;
    private String fileName;
    private String caption;
    private String date;
    private ArrayList<String> boundaryData;

    public HttpServletRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        StringBuilder request = new StringBuilder();
        while (inputStream.available() != 0) {
            request.append((char) inputStream.read());
        }
//        System.out.println(request);

        parseHeader(request.toString());
        parseBoundary(request.toString());
        if (boundaryData != null) parseFormData();
    }

    private void parseHeader(String request) {
        String[] stream = request.split("\n");
        if (stream[0].contains("GET")) method = "GET";
        else if (stream[0].contains("POST")) method = "POST";

        for (String line : stream) {
            String[] parsedLine = line.split(": ");
            if (parsedLine[0].equals("User-Agent")) userAgent = parsedLine[1];
            if (parsedLine[0].equals("Content-Type")) contentType = parsedLine[1].split(";")[0];
        }

    }

    private void parseBoundary(String request) {
        if (this.method != null && this.method.equals("POST")) {
            String[] stream = request.split("\n");
            boundaryData = new ArrayList<>();
            int i = 0;

            while (i < stream.length) {
                while (!stream[i].contains("------WebKitFormBoundary")) i++;
                i++;
                while (i < stream.length && !stream[i].contains("------WebKitFormBoundary")) {
                    if (!stream[i].equals("")) boundaryData.add(stream[i]);
                    i++;
                }
            }
        }
    }

    private void parseFormData() {
        String fileBytes = "";
        String caption = "";
        String date = "";

        int i = 0;

        while (i < boundaryData.size() && boundaryData.get(i).contains("Content-Disposition")) {
            fileName = boundaryData.get(i).split("\"")[boundaryData.get(i).split("\"").length - 2];
            i++;
            if (boundaryData.get(i).contains("Content-Type")) {
                i++;
                while (!boundaryData.get(i).contains("Content-Disposition")) {
                    fileBytes += boundaryData.get(i++);
                }
            }

            if (boundaryData.get(i).contains("caption")) {
                i += 2;
                caption += boundaryData.get(i);
            }

            if (boundaryData.get(++i).contains("date")) {
                i += 2;
                date += boundaryData.get(i);
            }
        }

        fileArray = fileBytes.getBytes();
        this.caption = caption.trim();
        this.date = date.trim();

        System.out.println("file byte array: " + fileArray);
        System.out.println("caption: " + caption);
        System.out.println("date: " + date);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMethod() {
        return this.method;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public byte[] getFileArray() {
        return this.fileArray;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getDate() {
        return this.date;
    }

    public String getConnectionAgent() {
        if (userAgent == null) return "null";
        if (userAgent.contains("Mozilla")) return "Web";
        return "Console";
    }

}