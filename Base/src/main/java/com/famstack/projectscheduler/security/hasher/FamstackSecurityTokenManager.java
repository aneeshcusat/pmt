package com.famstack.projectscheduler.security.hasher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.Logger;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.util.StringUtils;

public class FamstackSecurityTokenManager extends BaseFamstackService
{

    /** The Constant STRING_COLUMN. */
    private static final String STRING_COLUMN = ":";

    /** The Constant AES. */
    private static final String AES = "AES";

    /** The logger. */
    private static Logger logger = getStaticLogger(FamstackSecurityTokenManager.class);

    /**
     * Encrypt.
     * 
     * @param secretPasswordString the secret password string
     * @param hashKey the hash key
     * @return the string
     */
    public static String encryptStringWithDate(String secretPasswordString, String hashKey)
    {
        String secretPassword = secretPasswordString;
        String encryptedString = null;
        if (StringUtils.isNotBlank(secretPassword) && StringUtils.isNotBlank(hashKey)) {
            secretPassword += STRING_COLUMN + new Date().getTime();
            encryptedString = encryptString(secretPassword, hashKey);
        }
        return encryptedString;
    }

    /**
     * Encrypt string.
     * 
     * @param secretPasswordString the secret password string
     * @param hashKey the hash key
     * @return the string
     */
    public static String encryptString(String secretPasswordString, String hashKey)
    {
        String secretPassword = secretPasswordString;
        String encryptedString = null;
        if (StringUtils.isNotBlank(secretPassword) && StringUtils.isNotBlank(hashKey)) {

            SecretKeySpec secretSpec = new SecretKeySpec(hashKey.getBytes(), AES);
            try {

                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(Cipher.ENCRYPT_MODE, secretSpec);

                byte[] encryptedTextBytes = cipher.doFinal(secretPassword.getBytes("UTF-8"));

                encryptedString = DatatypeConverter.printBase64Binary(encryptedTextBytes);
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }

        }
        return encryptedString;
    }

    /**
     * Decrypt.
     * 
     * @param securityToken the security token
     * @param hashKey the hash key
     * @return the string
     */
    private static String decrypt(String securityToken, String hashKey)
    {
        String decryptedString = null;
        if (StringUtils.isNotBlank(securityToken) && StringUtils.isNotBlank(hashKey)) {
            try {
                byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(securityToken);
                Key aesKey = new SecretKeySpec(hashKey.getBytes(), AES);
                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(Cipher.DECRYPT_MODE, aesKey);

                byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

                decryptedString = new String(decryptedTextBytes);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return decryptedString;
    }

    /**
     * Validate security token.
     * 
     * @param clientCode the client code
     * @param encryptedString the encrypted string
     * @param hashkey the hashkey
     * @return true, if successful
     */
    public static boolean validateSecurityToken(String password, String encryptedString, String hashkey)
    {
        String decryptedString = decrypt(encryptedString, hashkey);
        int hour = 24;
        if (decryptedString != null) {
            String[] passDate = decryptedString.split(STRING_COLUMN);
            long remoteTime = Long.parseLong(passDate[1]);
            String decryptedClientId = passDate[0];
            long currentTime = new Date().getTime();

            long timeDiff = currentTime - remoteTime;
            logger.debug("time diff :" + timeDiff);
            logger.debug("time diff/100 :" + timeDiff / 1000);
            if (timeDiff / 1000 <= 60 * 60 * hour && decryptedClientId.equalsIgnoreCase(hashkey)) {
                logger.debug("Valid key");
                return true;
            }
        }
        return false;
    }
}
