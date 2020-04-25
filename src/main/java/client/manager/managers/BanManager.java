package client.manager.managers;

import client.alt.Alt;
import client.alt.Ban;
import client.manager.AbstManager;
import client.utils.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanManager extends AbstManager {
    private static final File DIR = FileUtils.getConfigFile("banned");
    public static List<Ban> list = new ArrayList<>();

    public static void init() {
        BanManager.loadBans();
        BanManager.saveBans();
    }

    public static void loadBans() {
        List<String> fileContent = FileUtils.read(DIR);
        list.clear();
        for (String line : fileContent) {
            try {
                String[] split = line.split(";");
                String email = split[0];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = sdf.parse(split[1]);

                list.add(new Ban(email, date));
                continue;
            }
            catch (ParseException e) {
                // empty catch block
            }
        }
    }

    public static void saveBans() {
        List<String> fileContent = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (Ban ban : list) {
            String alt = ban.email.toString();
            String date = sdf.format(ban.date);
            String str = String.format("%s;%s", alt, date);

            fileContent.add(str);
        }
        FileUtils.write(DIR, fileContent, true);
    }

    public static void setBan(Alt alt, Date date) {
        addBan(new Ban(alt.email, date));
    }

    public static void addBan(Ban ban) {
        for (Ban b : list) {
            if (!ban.email.equalsIgnoreCase(b.email)) continue;
            return;
        }
        list.add(ban);
    }

    public static Ban getBan(Alt alt) {
        for (Ban ban : list) {
            if (!ban.email.equalsIgnoreCase(alt.email)) continue;
            return ban;
        }
        return null;
    }

    public static void removeBan(Alt alt) {
        List<Ban> newList = new ArrayList<>();
        for (Ban ban : list) {
            if (!alt.email.equalsIgnoreCase(ban.email)) {
                newList.add(ban);
            }
        }
        list.clear();
        for (Ban ban : newList) {
            list.add(ban);
        }
    }

    public static void removeBan(Ban ban) {
        list.remove(ban);
    }

    public static String countdown(Ban ban) {
        long difference = ban.date.getTime() - new Date().getTime();
        int days = (int) ((difference / (1000*60*60*24)) % 365);
        int hours = (int) ((difference / (1000*60*60)) % 24);
        int minutes = (int) ((difference / (1000*60)) % 60);
        int seconds = (int) (difference / 1000) % 60 ;
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }
}
