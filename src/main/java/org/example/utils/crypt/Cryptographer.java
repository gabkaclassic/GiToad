package org.example.utils.crypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Cryptographer {

    private Cipher cipher;
    private SecretKey key;

    public Cryptographer(
            String algorithmKey,
            String algorithmCipher,
            String key
    ) throws NoSuchPaddingException, NoSuchAlgorithmException {

        this.key = new SecretKeySpec(key.getBytes(), algorithmKey);
        cipher = Cipher.getInstance(algorithmCipher);
    }

    private byte[] processString(byte[] input, int mode) {

        try {
            cipher.init(mode, key);

            return cipher.doFinal(input);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] input) { return (input == null) ? null : processString(input, Cipher.ENCRYPT_MODE); }
    public byte[] decrypt(byte[] input) { return  (input == null) ? null : processString(input, Cipher.DECRYPT_MODE); }

    public boolean matches(byte[] crypt, String value) { return Arrays.equals(decrypt(crypt), value.getBytes()); }
}
