package client.manager.managers;

import client.alt.Alt;
import client.alt.LoginThread;
import client.manager.AbstManager;
import client.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountManager extends AbstManager {
    private static final File ACCOUNT_DIR = FileUtils.getConfigFile("Accounts");
    public static List<Alt> accountList = new ArrayList<Alt>();
    public static final Alt nullAlt = new Alt("null", "null");

    public static void init() {
        AccountManager.loadAccounts();
        AccountManager.saveAccounts();
    }

    public static void loadAccounts() {
        List<String> fileContent = FileUtils.read(ACCOUNT_DIR);
        accountList.clear();
        for (String line : fileContent) {
            try {
                String[] split = line.split(":");
                String email = split[0];
                if (split.length == 1) {
                    accountList.add(new Alt(email, ""));
                    continue;
                }
                String pass = split[0];
                if (split.length > 1) {
                    email = split[0];
                    pass = split[1];
                    accountList.add(new Alt(email, pass));
                    continue;
                }
                accountList.add(new Alt(email, pass));
                continue;
            }
            catch (Exception split) {
                // empty catch block
            }
        }
    }

    public static void saveAccounts() {
        ArrayList<String> fileContent = new ArrayList<String>();
        FileUtils.write(ACCOUNT_DIR, fileContent, true);
        for (Alt alt : accountList) {
            String email = alt.email;
            String pass = alt.pass;

            fileContent.add(String.format("%s:%s", email, pass));
        }
        FileUtils.write(ACCOUNT_DIR, fileContent, true);
    }

    public static void random() {
        List<Alt> noStar = AccountManager.accountList;
        for (Alt alt : noStar) if (StarManager.getStar(alt).starred) noStar.remove(alt);

        if (noStar.size() < 1) return;

        Random random = new Random();
        int randomInt = random.nextInt(noStar.size() - 1);
        Alt alt = noStar.get(randomInt);
        LoginThread thread = new LoginThread(alt.email, alt.pass);
        thread.start();
    }

    public static void addAlt(Alt alt) {
        accountList.add(alt);
    }

    public static void addAlt(int pos, Alt alt) {
        accountList.add(pos, alt);
    }

    public static Alt getAlt(String email) {
        for (Alt account : accountList) {
            if (!account.email.equalsIgnoreCase(email)) continue;
            return account;
        }
        return nullAlt;
    }

    public static void removeAlt(Alt alt) {
        accountList.remove(alt);
    }
}
