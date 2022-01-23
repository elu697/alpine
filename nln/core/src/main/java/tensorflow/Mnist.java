package tensorflow;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Mnist {
    public static void main(String[] args) {
        String path = new File(".").getAbsoluteFile().getParent();
        String cmd = "python3 " + path + "/test.py";

        Runtime runtime = Runtime.getRuntime();
        try {
            Process process= runtime.exec(cmd);
            process.waitFor();
            process.getOutputStream();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
