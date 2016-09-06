package com.famstack.projectscheduler.security.hasher.generator;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

/**
 * The Class PasswordTokenGenerator.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class PasswordTokenGenerator {

    /** The Constant AB. */
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz=+-&*^%$#@()";

    /** The rnd. */
    static SecureRandom rnd = new SecureRandom();

    /**
     * Generate.
     *
     * @param length the length
     * @return the string
     */
    public String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
