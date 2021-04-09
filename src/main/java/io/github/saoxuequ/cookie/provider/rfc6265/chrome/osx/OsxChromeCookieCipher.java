package io.github.saoxuequ.cookie.provider.rfc6265.chrome.osx;

import io.github.saoxuequ.cookie.provider.cipher.Cipher;
import io.github.saoxuequ.cookie.provider.exception.CipherException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class OsxChromeCookieCipher implements Cipher {

    private final SecretKeySpec key;
    private final IvParameterSpec iv;

    /**
     *
     * @param password 执行"security find-generic-password -ga Chrome"命令获取password
     * @throws CipherException
     */
    public OsxChromeCookieCipher(String password) throws CipherException {
        try {
            this.iv = new IvParameterSpec("                ".getBytes("UTF-8"));
            this.key = new SecretKeySpec(generateKeyByte(password), "AES");
        } catch (Exception e) {
            throw new CipherException(e);
        }
    }

    private byte[] generateKeyByte(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
        SecretKeyFactory kFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(
                password.toCharArray(),
                "saltysalt".getBytes("UTF-8"),
                1003,
                128
        );
        return kFactory.generateSecret(pbeKeySpec).getEncoded();
    }

    private javax.crypto.Cipher getCipherInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    private javax.crypto.Cipher getEncryptCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        javax.crypto.Cipher cipher = getCipherInstance();
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, iv);
        return cipher;
    }

    private javax.crypto.Cipher getDecryptCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        javax.crypto.Cipher cipher = getCipherInstance();
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, iv);
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] code) throws CipherException {
        try {
            return getEncryptCipherInstance().doFinal(code);
        } catch (Exception e) {
            throw new CipherException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] code) throws CipherException {
        try {
            return getDecryptCipherInstance().doFinal(code);
        } catch (Exception e) {
            throw new CipherException(e);
        }
    }
}
