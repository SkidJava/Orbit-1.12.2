package client;

public class ClientInfo {

    private final static String clientName = "Orbit";
    private final static float clientBuild = 1.0f;

    private final static String resouceLocation = "client/";

    public static String getClientName() {
        return clientName;
    }

    public static String getClientBuild() {
        return "Version " + clientBuild;
    }

    public static String getFormatedName() {
        return getClientName() + " " + getClientBuild();
    }

    public static String getResouceLocation() {
        return resouceLocation;
    }
}
