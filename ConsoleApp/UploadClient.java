package ConsoleApp;

import java.io.*;
import java.net.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class UploadClient {

    private static String PATH = null;
    private static String CAPTION = null;
    private static String DATE = null;

    public String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    public String boundary = "------WebKitFormBoundary" + uuid + "\r\n";


    public UploadClient() {
    }

    private static void writeInFile(ArrayList<String> arrayList) {
        String describe = arrayList.get(0) + "&" + arrayList.get(1) + "@" + arrayList.get(2) + "*";
        Path currentRelativePath = Paths.get("");
        try (FileWriter fw = new FileWriter(currentRelativePath.toAbsolutePath() + "/images.txt", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(describe);
            bw.newLine();
            bw.close();
//            System.out.println(describe);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isPathValid(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }

    public String uploadFile() throws IOException {
        boolean end = false;
        while (!end) {
            Socket socket = new Socket("localhost", 8080);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            PATH = null;
            CAPTION = null;
            DATE = null;

            //Here
            Scanner scanner = new Scanner(System.in);
            System.out.println("Upload image direction: ");
            PATH = scanner.nextLine();
            System.out.println("Caption: ");
            CAPTION = scanner.nextLine();
            System.out.println("Date: ");
            DATE = scanner.nextLine();
//                System.out.println("Path: " + PATH + ", Caption: " + CAPTION + ", Date: " + DATE);
            try {
                FileInputStream fis = new FileInputStream(PATH);
                byte[] bytes = fis.readAllBytes();
                if (isPathValid(PATH)) {
                    ArrayList<String> myArraylist = new ArrayList<>();
                    myArraylist.add(PATH);
                    myArraylist.add(CAPTION);
                    if (DATE.length() < 1) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        LocalDate now = LocalDate.now();
                        DATE = String.valueOf(now);
                        myArraylist.add(dtf.format(now));
                    } else {
                        myArraylist.add(DATE);
                    }
                    writeInFile(myArraylist);

                    String name = PATH.split("/")[PATH.split("/").length - 1];
//                    out.write(getRequestHeader(name).getBytes());
                    out.write(bytes);
//                    out.write(getRequestBody(CAPTION, DATE).getBytes());
                }
                socket.shutdownOutput();
                byte[] content = new byte[1];
                int bytesRead = -1;
                while ((bytesRead = in.read(content)) != -1) {
                    System.out.print((char) content[0]);
                }
                socket.shutdownInput();
                fis.close();
                System.out.println("Successfully updated image " + CAPTION);

            } catch (Exception e) {
                System.out.println("ERROR");
                System.err.println(e.getMessage());

            }

            System.out.println("\r\nDo you want to upload more image?(Y/N) ");
            String tf = scanner.nextLine();
            if (!(tf.equalsIgnoreCase("Y") || tf.equalsIgnoreCase("yes"))) {
                end = true;
            }
        }

        return "Shut down the terminal";
    }

    private String getRequestHeader(String name) {
        return "POST / HTTP/1.1\r\n" +
                "Content-Type: multipart/form-data; boundary=" + boundary +
                "User-Agent: Console\r\n" +
                "\r\n" +
                boundary +
                "Content-Disposition: form-data; name=\"fileName\"; filename=\"" + name + "\"\r\n" +
                "Content-Type: image/png\r\n" +
                "\r\n";
    }

    private String getRequestBody(String caption, String date) {
        return "\r\n" + boundary +
                "Content-Disposition: form-data; name=\"caption\"\r\n" +
                "\r\n" + caption +
                "\r\n" +
                boundary +
                "Content-Disposition: form-data; name=\"date\"\r\n" +
                "\r\n" + date +
                "\r\n" + boundary.trim()+
                "--\r\n";
    }
}
