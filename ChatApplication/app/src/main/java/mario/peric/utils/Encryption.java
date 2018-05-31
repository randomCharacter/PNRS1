package mario.peric.utils;


public class Encryption {

    static {
        System.loadLibrary("Encryption");
    }

    public static final String KEY = "This is the key";

    public native String encrypt(String value, String key);

    public native String decrypt(String value, String key);

}
