package com.example.backend.service;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    //     In cryptography, a "salt" is a random or semi-random data that is added to the input of a one-way function (such as a hash function)
    //     to make it more difficult to reverse engineer. The salt is usually a non-secret value that is stored alongside the encrypted data.
    @Value("${encryption.salt.rounds}")
    // BCrypt is a popular cryptographic algorithm used for password hashing. When using BCrypt, the "saltrounds"
    // parameter determines the number of iterations of the key derivation function that are performed.
    // The higher the number of saltrounds, the more secure the resulting hash is against brute-force attacks.
    private int saltRounds;
    private String salt;

    @PostConstruct
    public void postConstruct(){
        // BCrypt.gensalt() is a method provided by the BCrypt library that generates a random salt string that can be used to hash a password.
        salt = BCrypt.gensalt(saltRounds);
    }

    public String encryptPassword(String password){
        // BCrypt.hashpw() is a method provided by the BCrypt password hashing library that allows you to hash a password
        // using the BCrypt algorithm with a specified salt value and number of saltrounds.
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hash){
        // BCrypt.checkpw() is a method provided by the BCrypt password hashing library that allows you to verify a plain
        // text password against a previously hashed password.
        return BCrypt.checkpw(password, hash);
    }

}
