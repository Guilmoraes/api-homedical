package br.com.homedical.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class GenerateS3NameUtil {

    private GenerateS3NameUtil() {
    }

    /**
     * Generate a name.
     *
     * @return the generated name
     */
    public static String generateName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Name is null");
        }

        String hash;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(fileName.getBytes());
            byte[] digest = md.digest();
            hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hash = UUID.randomUUID().toString();
        }

        return hash;
    }

    public static String generateName(String fileName, String alg) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Name is null");
        }

        String hash;

        try {
            MessageDigest md = MessageDigest.getInstance(alg);
            md.update(fileName.getBytes());
            byte[] digest = md.digest();
            hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hash = UUID.randomUUID().toString();
        }

        return hash;
    }
}
