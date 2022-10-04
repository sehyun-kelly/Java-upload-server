import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
            OutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + "/images/" + milliSeconds + ".png");
            baos.writeTo(outputStream);
            outputStream.close();
            PrintWriter out = new PrintWriter(response.getOutputStream(), true);
            File dir = new File(System.getProperty("user.dir") + "/images");
            String[] chld = dir.list();
            for (int i = 0; i < chld.length; i++) {
                String fileName = chld[i];
                out.println(fileName + "\n");
                System.out.println(fileName);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}