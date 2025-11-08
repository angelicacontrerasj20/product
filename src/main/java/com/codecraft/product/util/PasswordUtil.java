package com.codecraft.product.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Utilidad para encriptar y desencriptar contraseñas usando AES.
 */
public class PasswordUtil {
    private static final String SECRET_KEY = "MySecretKey12345"; // 16 caracteres para AES-128
    private static final String ALGORITHM = "AES";

    /**
     * Encripta una contraseña en texto plano.
     * @param plainText Contraseña en texto plano.
     * @return Contraseña encriptada en Base64.
     */
    public static String encrypt(String plainText) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    /**
     * Desencripta una contraseña encriptada en Base64.
     * @param encryptedText Contraseña encriptada en Base64.
     * @return Contraseña en texto plano.
     */
    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar la contraseña", e);
        }
    }
}
