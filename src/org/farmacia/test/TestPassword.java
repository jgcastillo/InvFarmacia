package org.farmacia.test;

import org.farmacia.utils.SecurityUtil;

/**
 *
 * @author jgcastillo
 */
public class TestPassword {
    public static void main(String[] args) {
        String psw = "spontecorp";
        String encrypted = SecurityUtil.getSecurePassword(psw);
        System.out.println(encrypted);
    }
}
