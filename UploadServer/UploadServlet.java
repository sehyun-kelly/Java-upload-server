import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;


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
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(currentRelativePath.toAbsolutePath().getParent() + "/ConsoleApp/images.txt"));
                String line = reader.readLine();
                while (line != null) {
                    int i, j, k;
                    System.out.print("{" + '"' + "Path:" + '"');
                    for (i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == '&') {
                            System.out.print('"' + ", " + '"' + "Caption:" + '"' + ':' + '"');
                            break;
                        }
                        System.out.print(line.charAt(i));
                    }
                    for (j = i + 1; j < line.length(); j++) {
                        if (line.charAt(j) == '@') {
                            System.out.print('"' + ", " + '"' + "Date:" + '"' + ':' + '"');
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
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                System.out.println(e);
            }

//            File dir = new File(currentRelativePath.toAbsolutePath().getParent() + "/images");
//            String[] chld = dir.list();
//            for (int i = 0; i < chld.length; i++) {
//                String fileName = chld[i];
//                out.println(fileName + "\n");
//                System.out.println(fileName);
//            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}