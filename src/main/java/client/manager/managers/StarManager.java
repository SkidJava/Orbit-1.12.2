package client.manager.managers;

import client.alt.Alt;
import client.alt.Star;
import client.manager.AbstManager;
import client.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StarManager extends AbstManager {
    private static final File DIR = FileUtils.getConfigFile("starred");
    public static List<Star> list = new ArrayList<>();

    public static void init() {
        StarManager.loadStars();
        StarManager.saveStars();
    }

    public static void loadStars() {
        List<String> fileContent = FileUtils.read(DIR);
        list.clear();
        for (String line : fileContent) {
            String[] split = line.split(":");
            boolean star = Boolean.valueOf(split[1]);

            list.add(new Star(AccountManager.getAlt(split[0]), star));
        }
    }

    public static void saveStars() {
        List<String> fileContent = new ArrayList<>();
        for (Star star : list) {
            String email = star.alt.email;
            String starred = String.valueOf(star.starred);
            String str = String.format("%s:%s", email, starred);

            fileContent.add(str);
        }
        FileUtils.write(DIR, fileContent, true);
    }

    public static void setStar(Alt acc, boolean star) {
        addStar(new Star(acc, star));
    }

    public static void addStar(Star star) {
        List<Star> newList = new ArrayList<>();
        for (Star str : list) {
            if (!star.alt.email.equalsIgnoreCase(str.alt.email)) {
                newList.add(str);
            }
        }
        list.clear();
        for (Star str : newList) {
            list.add(str);
        }
        list.add(star);
    }

    public static Star getStar(Alt alt) {
        for (Star star: list) {
            if (!star.alt.email.equalsIgnoreCase(alt.email)) continue;
            return star;
        }
        return new Star(null, false);
    }

    public static void removeStar(Alt alt) {
        List<Star> newList = new ArrayList<>();
        for (Star star: list) {
            if (!alt.email.equalsIgnoreCase(star.alt.email)) {
                newList.add(star);
            }
        }
        list.clear();
        for (Star star : newList) {
            list.add(star);
        }
    }

    public static void removeStar(Star star) {
        list.remove(star);
    }
}
