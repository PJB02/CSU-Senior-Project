/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

//Student Name: Jacob Braddock
//Program Name: File Encryptor
//Creation Date: December 20th, 2024
//Last Modified Date: April 19th, 225
//CSCI Course:  499
//Grade Received:  100
//Design Comments: This is a file encryptor that is used to encrypt and decrypt files

package seniorproject;

/**
 *
 * @author jacobbraddock
 */
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;

public class EncryptorUtils {

    public static void encrypt(File inputFile, String password, File outputDir) throws Exception {
        byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);
        byte[] iv = SecureRandom.getInstanceStrong().generateSeed(12);

        SecretKey key = generateKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        String outputFileName = inputFile.getName() + ".enc";
        File encryptedFile = new File(outputDir, outputFileName);

        try (
            FileOutputStream fos = new FileOutputStream(encryptedFile);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            FileInputStream fis = new FileInputStream(inputFile)
        ) {
            fos.write(salt); // write salt first
            fos.write(iv);   // then IV

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }

        Files.delete(inputFile.toPath()); // delete original
    }

    public static void decrypt(File inputFile, String password, File outputDir) throws Exception {
        if (inputFile.length() <= 28) {
            throw new IllegalArgumentException("Invalid encrypted file: too short to contain salt and IV.");
        }

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] salt = new byte[16];
            byte[] iv = new byte[12];

            if (fis.read(salt) != 16 || fis.read(iv) != 12) {
                throw new IOException("Failed to read salt or IV from file.");
            }

            SecretKey key = generateKey(password, salt);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            String originalName = inputFile.getName().replaceFirst("\\.enc$", "");
            File decryptedFile = new File(outputDir, originalName);

            try (
                BufferedInputStream bis = new BufferedInputStream(fis);
                CipherInputStream cis = new CipherInputStream(bis, cipher);
                FileOutputStream fos = new FileOutputStream(decryptedFile)
            ) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }

        Files.delete(inputFile.toPath()); // delete encrypted file
    }

    private static SecretKey generateKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}
