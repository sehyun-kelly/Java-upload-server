//import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class UploadClient {

    private static String PATH = null;
    private static String CAPTION = null;
    private static String DATE = null;

    public UploadClient() {
    }

    private static void writeInFile(ArrayList<String> arrayList) {
        String describe = arrayList.get(0) + "&" + arrayList.get(1) + "@" + arrayList.get(2) + "*";
        try (FileWriter fw = new FileWriter("images.txt", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(describe);
            bw.newLine();
            bw.close();
//            System.out.println(describe);

        } catch (IOException e) {
            System.out.println(e);
            System.out.println("file not found");
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

    public String uploadFile() {
        boolean end = false;
        while (!end) {
            boolean check = true;
            try {
                PATH = null;
                CAPTION = null;
                DATE = null;
                Socket socket = new Socket("localhost", 8080);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();

                //Here
                Scanner myObj = new Scanner(System.in);
                System.out.println("Upload image direction: ");
                PATH = myObj.nextLine();
                boolean checkCaption = true;
                while (checkCaption) {
                    Scanner myObj2 = new Scanner(System.in);
                    System.out.println("Caption: ");
                    CAPTION = myObj2.nextLine();
                    if (CAPTION.length() > 0) {
                        checkCaption = false;
                    } else {
                        System.out.println("Caption can not be empty");
                    }
                }
                Scanner myObj3 = new Scanner(System.in);
                System.out.println("Date: ");
                DATE = myObj3.nextLine();
//                System.out.println("Path: " + PATH + ", Caption: " + CAPTION + ", Date: " + DATE);
                FileInputStream fis = new FileInputStream(PATH);
                byte[] bytes = fis.readAllBytes();
                if (isPathValid(PATH)) {
                    out.write(bytes);
                }
                socket.shutdownOutput();
                fis.close();
                socket.shutdownInput();
                System.out.println("Successfully updated image " + CAPTION);

            } catch (Exception e) {
                System.out.println("ERROR");
                System.err.println(e);
                check = false;

            }
            if (check) {
                ArrayList<String> myArraylist = new ArrayList<>();
                myArraylist.add(PATH);
                myArraylist.add(CAPTION);
                if (DATE.length() < 1) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    myArraylist.add(dtf.format(now));
                } else {
                    myArraylist.add(DATE);
                }
                writeInFile(myArraylist);
            }

            Scanner answer = new Scanner(System.in);
            System.out.println("\nDo you want to upload more image?(Y/N) ");
            String tf = answer.nextLine();
            if (!(tf.equals("Y") || tf.equals("y") || tf.equals("yes") || tf.equals("Yes"))) {
                end = true;
            }
        }

        return "Shut down the terminal";
    }
}


//                JSONObject imageInfo = new JSONObject();
//                imageInfo.put("Path", path);
//                imageInfo.put("Caption", caption);
//                imageInfo.put("Date", date);
//
//                JSONObject imageObject = new JSONObject();
//                imageObject.put(caption, imageInfo);
//
//                String pathJson = "image.json";
//
//                try (PrintWriter file = new PrintWriter(new FileWriter(pathJson))) {
//                    System.out.println(file);
//                    //We can write any JSONArray or JSONObject instance to the file
//                    file.write(imageObject.toJSONString());
//                    file.flush();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
