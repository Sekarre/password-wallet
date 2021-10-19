package com.example.passwordwallet.util;

public class FakePasswordFactory {

    public static String buildRandomFakePassword() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < Math.random() * 10 + 7; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }
}
