package web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class Web {

    public static String getPage(String page) {
        ClassLoader classLoader = Web.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(page)).getFile());
        String contents = null;
        try {
            contents = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            return contents;
        } catch (IOException ex) {
            return contents;
        }
    }
    public static String getPage(String page,String lenta,String auchan) {
        ClassLoader classLoader = Web.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(page)).getFile());
        String contents = null;
        try {
            contents = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            contents = contents.replace("%lenta%",lenta);
            contents = contents.replace("%auchan%",auchan);
            return contents;
        } catch (IOException ex) {
            return contents;
        }
    }

}
