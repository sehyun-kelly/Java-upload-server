package ConsoleApp;

public class Activity {
    public static void main(String[] args) {
        try {
            System.out.println(new UploadClient().uploadFile());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
