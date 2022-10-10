package UploadServer;

import HttpServlet.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.List;

import static UploadServer.UploadServer.logger;


public class UploadServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream baos = request.updateRequest(in);
            Clock clock = Clock.systemDefaultZone();
            long milliSeconds = clock.millis();
            Path currentRelativePath = Paths.get("");
            OutputStream outputStream = new FileOutputStream(currentRelativePath.toAbsolutePath() + "/images/" + milliSeconds + ".png");
            baos.writeTo(outputStream);
            outputStream.close();
            PrintWriter out = new PrintWriter(response.getOutputStream(), true);
            try {
                File file = new File(currentRelativePath.toAbsolutePath() + "/images.txt");
                List<String> lines = Files.readAllLines(file.toPath());
                lines.sort(null);
                StringBuilder json = new StringBuilder();
                json.append("{\n");
                int hasNext = 0, end = lines.size();
                for (String line : lines) {
                    hasNext++;
                    int i, j, k;
                    json.append("\t{" + '"' + "Path" + "\":\"");
                    for (i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == '&') {
                            json.append('"' + ", " + '"' + "Caption" + '"' + ':' + '"');
                            break;
                        }
                        json.append(line.charAt(i));
                    }
                    for (j = i + 1; j < line.length(); j++) {
                        if (line.charAt(j) == '@') {
                            json.append('"' + ", " + '"' + "Date" + '"' + ':' + '"');
                            break;
                        }
                        json.append(line.charAt(j));
                    }
                    for (k = j + 1; k < line.length(); k++) {
                        if (line.charAt(k) == '*') {
                            json.append('"' + "}")
                                    .append((hasNext == end) ? "" : ",")
                                    .append("\n");
                            break;
                        }
                        json.append(line.charAt(k));
                    }
                }
                json.append("}\n");
                response.stringResponse = json.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}