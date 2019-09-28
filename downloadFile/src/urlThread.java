import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class urlThread extends Thread {
    String strUrl;

    urlThread(String strUrl){
        this.strUrl = strUrl;
    }

    @Override
    public void run(){
        try {
            String extension = strUrl.substring(strUrl.lastIndexOf('/')+1);
            URL url = new URL(strUrl);
            InputStream inputStream = url.openStream();
            Files.copy(inputStream, new File("c:/" + extension).toPath());
        } catch (IOException e) {
            System.out.println("URL is invalid");
        }
    }
}
