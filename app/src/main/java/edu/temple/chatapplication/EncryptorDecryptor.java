package edu.temple.chatapplication;

import android.util.Base64;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptorDecryptor {
    KeyPair userKeyPair;
    //in PEM format
    String partnerPublicKey;

    public EncryptorDecryptor(KeyPair userKeyPair, String partnerPublicKey) {
        this.userKeyPair = userKeyPair;
        this.partnerPublicKey = partnerPublicKey;
    }

    //encrypt user's message using their private key
    public String getEncrypted(String plainText) {
        String encryptedText = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) userKeyPair.getPrivate();
            String privateKeyString = privateKey.getPrivateExponent().toString();
            RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(privateKey.getModulus(), new BigInteger(privateKeyString));
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte [] encrypted = cipher.doFinal(plainText.getBytes());
            encryptedText = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    //decrypt partner's message using their public key
    public String getDecrypted(String encryptedText) {
        String decryptedText = null;
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            //strip off PEM headers
            String strippedKey = partnerPublicKey
                    .replace("-----BEGIN PUBLIC KEY-----\n", "")
                    .replace("-----END PUBLIC KEY-----", "");

            byte[] encoded = Base64.decode(strippedKey, Base64.DEFAULT);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

            String rsaPublicKeyString = rsaPublicKey.getPublicExponent().toString();
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaPublicKey.getModulus(), new BigInteger(rsaPublicKeyString));
            rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            decryptedText = new String(cipher.doFinal(encryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
