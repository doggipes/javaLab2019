import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            urlThread thread = new urlThread(args[i]);
            thread.start();
        }
        }
    }

