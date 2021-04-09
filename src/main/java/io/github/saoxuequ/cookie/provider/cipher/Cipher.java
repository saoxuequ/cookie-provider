package io.github.saoxuequ.cookie.provider.cipher;

import io.github.saoxuequ.cookie.provider.exception.CipherException;

public interface Cipher {
    byte[] encrypt(byte[] code) throws CipherException;
    byte[] decrypt(byte[] code) throws CipherException;
}
