package client.alt;

import client.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Altening {
    private static final File DIR = FileUtils.getConfigFile("Altening");
    public static String apikey = FileUtils.read(DIR).size() == 0 ? "§7API Key" : FileUtils.read(DIR).get(0);

    public Altening(String key) {
        List<String> list = new ArrayList<>();
        list.add(apikey = key);
        FileUtils.write(DIR, list,true);
    }
}
