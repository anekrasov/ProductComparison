package web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
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
    public static String getPage(String page,String lenta,String auchan,String metrocc) {
        ClassLoader classLoader = Web.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(page)).getFile());
        String contents = null;
        try {
            contents = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            contents = contents.replace("%lenta%",lenta);
            contents = contents.replace("%auchan%",auchan);
            contents = contents.replace("%metrocc%",metrocc);
            return contents;
        } catch (IOException ex) {
            return contents;
        }
    }
    public static String getPage(String page, HashMap<String,String> shopSelected) {
        ClassLoader classLoader = Web.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(page)).getFile());
        File lenta_table_template = new File(Objects.requireNonNull(classLoader.getResource("lenta_table.html")).getFile());
        File auchan_table_template = new File(Objects.requireNonNull(classLoader.getResource("auchan_table.html")).getFile());
        File metrocc_table_template = new File(Objects.requireNonNull(classLoader.getResource("metrocc_table.html")).getFile());
        String contents = "";
        String lenta_table;
        String auchan_table;
        String metrocc_table;
        String [] tags = new String[]{"%metrocc_table%","%lenta_table%","%auchan_table%","%lenta%","%auchan%","%metrocc%"};
        try {
            contents = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            lenta_table = Files.readString(lenta_table_template.toPath(), StandardCharsets.UTF_8);
            auchan_table = Files.readString(auchan_table_template.toPath(), StandardCharsets.UTF_8);
            metrocc_table = Files.readString(metrocc_table_template.toPath(), StandardCharsets.UTF_8);
            for (String shop: shopSelected.keySet()) {
                switch (shop){
                    case "lenta":
                        lenta_table = lenta_table.replace("%lenta%", shopSelected.get(shop));
                        contents = contents.replace("%lenta_table%",lenta_table);
                        break;
                    case "auchan":
                        auchan_table = auchan_table.replace("%auchan%", shopSelected.get(shop));
                        contents = contents.replace("%auchan_table%",auchan_table);
                        break;
                    case "metrocc":
                        metrocc_table = metrocc_table.replace("%metrocc%", shopSelected.get(shop));
                        contents = contents.replace("%metrocc_table%",metrocc_table);
                        break;
                    default:
                        break;
                }
            }
            for (String tag: tags) {
                contents = contents.replace(tag,"");
            }
        }catch (IOException ex){
            return contents;
        }
        return contents;

    }

}
