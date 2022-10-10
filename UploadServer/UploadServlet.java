import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] content = new byte[1];
            int bytesRead = -1;
            while ((bytesRead = in.read(content)) != -1) {
                baos.write(content, 0, bytesRead);
            }
            Clock clock = Clock.systemDefaultZone();
            long milliSeconds = clock.millis();
            Path currentRelativePath = Paths.get("");
            OutputStream outputStream = new FileOutputStream(currentRelativePath.toAbsolutePath().getParent() + "/images/" + milliSeconds + ".png");

            baos.writeTo(outputStream);
            outputStream.close();
            PrintWriter out = new PrintWriter(response.getOutputStream(), true);
            try {
                File file = new File(currentRelativePath.toAbsolutePath().getParent() + "/images.txt");
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
            Logger logger = Logger.getLogger(e.getMessage());
            logger.addHandler(UploadServer.fh);
            SimpleFormatter formatter = new SimpleFormatter();
            UploadServer.fh.setFormatter(formatter);
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}