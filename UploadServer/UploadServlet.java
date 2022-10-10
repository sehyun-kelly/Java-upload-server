package UploadServer;

import HttpServlet.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
                for (String line : lines) {
                    int i, j, k;
                    System.out.print("{" + '"' + "Path" + "\":");
                    for (i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == '&') {
                            System.out.print('"' + ", " + '"' + "Caption" + '"' + ':' + '"');
                            break;
                        }
                        System.out.print(line.charAt(i));
                    }
                    for (j = i + 1; j < line.length(); j++) {
                        if (line.charAt(j) == '@') {
                            System.out.print('"' + ", " + '"' + "Date" + '"' + ':' + '"');
                            break;
                        }
                        System.out.print(line.charAt(j));
                    }
                    for (k = j + 1; k < line.length(); k++) {
                        if (line.charAt(k) == '*') {
                            System.out.print('"' + "}" + "\n");
                            break;
                        }
                        System.out.print(line.charAt(k));
                    }

//                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}