import java.io.*;
import java.net.*;

public class UploadClient {
    public UploadClient() {
    }

    public String uploadFile() {
        String listing = "";
        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream("/Users/nngoc2701/Documents/Term 3/COMP-3940/Assignment II/A2DesignReference/ConsoleApp/AndroidLogo.png");
            byte[] bytes = fis.readAllBytes();
            out.write(bytes);
            socket.shutdownOutput();
            fis.close();
            System.out.println("Came this far\n");
            String filename = "";
            while ((filename = in.readLine()) != null) {
                listing += filename + '\n';
            }
            socket.shutdownInput();
        } catch (Exception e) {
            System.err.println(e);
        }
        return listing;
    }
}