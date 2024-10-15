package tp1_javafx.tp1_prog2_v2.Utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordEncrypter {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /* method to encrypt the user password */

    public static String encrypt(String data)
    {
        byte[] encryptedData = hash(data.toCharArray());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    /* encryptation algorithm*/
    public static byte[] hash(char[] data) {
        PBEKeySpec spec = new PBEKeySpec(data, "salt".getBytes(), ITERATIONS, KEY_LENGTH);
        Arrays.fill(data, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing data: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }
}
