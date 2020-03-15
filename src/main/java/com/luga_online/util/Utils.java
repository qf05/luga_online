package com.luga_online.util;

import com.vk.api.sdk.objects.users.UserXtrCounters;

public class Utils {

    public static void pauseVk() {
        try {
            Thread.sleep(335);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getUserVkName(UserXtrCounters user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
