package eif.viko.lt.predictionappclient;

import java.util.prefs.Preferences;

public class SecureStorage {
    private static final Preferences prefs = Preferences.userNodeForPackage(SecureStorage.class);

    public static void saveToken(String token) {
        prefs.put("token", token);
    }
    public static String getToken() {
        return prefs.get("token", null);
    }
    public static void clearToken() {
        prefs.remove("token");
    }
}
