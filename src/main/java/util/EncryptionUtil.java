package util;

import org.jasypt.util.text.BasicTextEncryptor;

public class EncryptionUtil {
    private static final String KEY = "#1998YUwa";

    public static String encrypt(String data) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(KEY);
        return encryptor.encrypt(data);
    }

    public static String decrypt(String encryptedData) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(KEY);
        return encryptor.decrypt(encryptedData);
    }
}
