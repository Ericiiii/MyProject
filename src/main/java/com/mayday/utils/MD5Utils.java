package com.mayday.utils;


import java.security.MessageDigest;

public class MD5Utils {

        private static final String SALT = "mayday";

        public static String encode(String password) {
         //   password = password + SALT;
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            char[] charArray = password.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++)
                byteArray[i] = (byte) charArray[i];
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        }

        public static void main(String[] args) {

            String message="卿青是头猪～";

            System.out.println("加密前信息为："+message);

            System.out.println("加密后信息为："+MD5Utils.encode(message));


        }

}
