package client;

public class ClientInfo {

    private final static String clientName = "Orbit";
    private final static int clientBuild = 1;

    private final static String resouceLocation = "client/";

    public static String getClientName() {
        return clientName;
    }

    public static String getClientBuild() {
        return "Build " + clientBuild;
    }

    public static String getFormatedName() {
        return getClientName() + " " + getClientBuild();
    }

    public static String getResouceLocation() {
        return resouceLocation;
    }
}
