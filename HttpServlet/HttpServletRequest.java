package HttpServlet;

import java.io.*;
import java.util.ArrayList;

public class HttpServletRequest {
    private final InputStream inputStream;
    private final ArrayList<Integer> raws;
    private String method;
    private String contentType;
    private int contentLength;
    private String userAgent;
    private byte[] fileArray;
    private String fileName;
    private String caption;
    private String date;
    private ArrayList<String> boundaryData;

    private int begPos;


    public HttpServletRequest(InputStream inputStream) throws IOException {
        this.raws = new ArrayList<>();
        this.inputStream = inputStream;

        StringBuilder request = new StringBuilder();
        while (inputStream.available() != 0) {
            int temp = inputStream.read();
            raws.add(temp);
            char input = (char) temp;
            request.append(input);
        }
        System.out.println(request);
        parseHeader(request.toString());
        parseBoundary(request.toString());
        if (boundaryData != null) parseFormData();
    }

    public ByteArrayOutputStream updateRequest(InputStream in) throws IOException, InterruptedException {
        StringBuilder request = new StringBuilder();
        raws.clear();
        byte[] content = new byte[1];
        int bytesRead = -1;
        while ((bytesRead = in.read(content)) != -1) {
            raws.add((int) content[0]);
            request.append((char) content[0]);
        }

        while (in.available() != 0) {
            wait();
        }

        parseHeader(request.toString());
        parseBoundary(request.toString());
        if (boundaryData != null) parseFormData();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(fileArray);

        return baos;
    }

    private void parseHeader(String request) {
        String[] stream = request.split("\n");
        if (stream[0].contains("GET")) method = "GET";
        else if (stream[0].contains("POST")) method = "POST";

        for (String line : stream) {
            String[] parsedLine = line.split(": ");
            if (parsedLine[0].equals("User-Agent")) userAgent = parsedLine[1];
            if (parsedLine[0].equals("Content-Length")) contentLength = Integer.parseInt(parsedLine[1].trim());
            if (parsedLine[0].equals("Content-Type")) contentType = parsedLine[1].split(";")[0];
        }
    }

    private void parseBoundary(String request) {
        begPos = 0;
        ArrayList<Integer> lineIndex = new ArrayList<>();
        if (this.method != null && this.method.equals("POST")) {
            String[] stream = request.split("\n");
            boundaryData = new ArrayList<>();
            int i = 0;

            while (i < stream.length) {
                while (i < stream.length && !stream[i].contains("------WebKitFormBoundary")){
                    begPos += stream[i].length() + 1;
                    i++;
                }
                lineIndex.add(i);
                i++;

                while (i < stream.length && !stream[i].contains("------WebKitFormBoundary")) {
                    if (!stream[i].equals("")){
                        boundaryData.add(stream[i]);
                    }
                    i++;
                }
            }

            parseFileArray(stream, lineIndex);
        }
    }

    private void parseFileArray(String[] stream, ArrayList<Integer> lineIndex){
        for(int k = 0; k < 4; k++){
            begPos += stream[lineIndex.get(0) + k].length() + 1;
        }

        int endPos = begPos;
        for(int index = lineIndex.get(0); index < lineIndex.get(1); index++){
            endPos += stream[index].length() + 1;
        }

        fileArray = new byte[endPos - begPos];

        int index = 0;
        for(int j = begPos; j < endPos; j++){
            fileArray[index] = raws.get(j).byteValue();
            index++;
        }
    }

    private void parseFormData() {
        StringBuilder caption = new StringBuilder();
        StringBuilder date = new StringBuilder();

        int i = 0;

        while (i < boundaryData.size() && boundaryData.get(i).contains("Content-Disposition")) {
            fileName = boundaryData.get(i).split("\"")[boundaryData.get(i++).split("\"").length - 2];
            if (boundaryData.get(i++).contains("Content-Type")) {
                while (!boundaryData.get(i).contains("Content-Disposition")) {
                    i++;
                }
            }

            if (boundaryData.get(i).contains("caption")) {
                i += 2;
                caption.append(boundaryData.get(i));
            }

            if (boundaryData.get(++i).contains("date")) {
                i += 2;
                date.append(boundaryData.get(i));
            }
        }

        this.caption = caption.toString().trim();
        this.date = date.toString().trim();

        System.out.println("file name: " + fileName);
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

    public int getContentLength() {
        return this.contentLength;
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