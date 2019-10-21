package ru.javalab.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class urlThread extends Thread {
    String strUrl;

   public urlThread(String strUrl){
        this.strUrl = strUrl;
    }

    @Override
    public void run(){
        try {
            URL url = new URL(strUrl);
            InputStream inputStream = url.openStream();
            isFileExists(inputStream, strUrl);
        } catch (IOException e) {
            System.out.println("URL is invalid " + strUrl);
            System.out.println(e);
        }
    }

    private synchronized void isFileExists(InputStream inputStream, String extension) throws IOException {
        extension = extension.substring(strUrl.lastIndexOf('/')+1);
        File file = new File("C:\\Users\\Джалил\\Desktop\\" + extension);
        while(file.exists()){
            String name = extension.substring(0, extension.indexOf("."));
            String extensionOld = extension.substring(extension.indexOf("."));

            if(name.contains("_") && !name.endsWith("_"))
            {
                String check = name.substring(name.lastIndexOf("_") + 1);
                if(isNumber(check)){
                        int num = Integer.parseInt(check);
                        extension = name.substring(0, name.lastIndexOf("_") + 1) + (num + 1) + extensionOld;
                }
                else{
                    extension = name + "_1" + extensionOld;
                }
            }
            else
            {
                extension = name + "_1" + extensionOld;
            }

            file = new File("C:\\Users\\Джалил\\Desktop\\" + extension);
        }

        Files.copy(inputStream, new File("C:\\Users\\Джалил\\Desktop\\" + extension).toPath());
    }

    private boolean isNumber(String number){
        return number.matches("-?\\d+(\\.\\d+)?");
    }
}
