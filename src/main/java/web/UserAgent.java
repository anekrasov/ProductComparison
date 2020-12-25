package web;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UserAgent {

    public static String getRandomUserAgent() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("useragent.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File no found");
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<>();
        if (sc != null) {
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        }
        int i = new Random().ints(0, lines.size()).findFirst().getAsInt();
        return lines.get(i);
    }
}
