package GlobalParameters;
//CreateTime: 2023-01-23 4:15 p.m.

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StaticMethod {
    public static ImageIcon readImage(String path){
        InputStream input = StaticMethod.class.getClassLoader().getResourceAsStream(path);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while (true) {
            try {
                if (-1 == (n = input.read(buffer))) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            output.write(buffer, 0, n);
        }
        return new ImageIcon(output.toByteArray());
    }
}
