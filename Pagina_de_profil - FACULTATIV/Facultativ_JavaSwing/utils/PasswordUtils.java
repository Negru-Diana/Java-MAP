package utils;

import exceptions.ServiceException;

import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    //Metoda pentru a "hashui" o parola
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        //Generare salt random
        byte[] salt = getSalt();

        //Parametri pentru PBKDF2
        int iterations = 10000; //Numarul de iteratii pentru functia PBKDF2
        int keyLength = 256; //Lungimea cheii in biti

        // Folosirea PBKDF2 pentru a hashui parola cu salt
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        try {
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Codificarea in Base64 a saltului si a parolei hashuite
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }



    //Metoda pentru a verifica parola (compara parola introdusa cu hash-ul salvat)
    public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedPasswordHash = Base64.getDecoder().decode(parts[1]);

        // Parametri pentru PBKDF2
        int iterations = 10000;  // Numarul de iteratii pentru functia PBKDF2
        int keyLength = 256;  // Lungimea cheii in biti

        // Folosirea aceleasi metode de hash pentru a obtine hash-ul pentru parola introdusa
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        try {
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] passwordHash = factory.generateSecret(spec).getEncoded();

            // Compararea hash-urilor
            return java.util.Arrays.equals(storedPasswordHash, passwordHash);
        } catch (Exception e) {
            throw new ServiceException("A aparut o eroare la verificarea parolei!");
        }
    }



    //Metoda pentru a obtine un salt random
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];  // Lungimea saltului (16 bytes)
        sr.nextBytes(salt);
        return salt;
    }
}
