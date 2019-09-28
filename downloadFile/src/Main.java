import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String strUrls = in.nextLine();
        String[] arrUrl = strUrls.split(" ");
        for (int i = 0; i < arrUrl.length; i++) {
            urlThread thread = new urlThread(arrUrl[i]);
            thread.start();
        }
    }
}
