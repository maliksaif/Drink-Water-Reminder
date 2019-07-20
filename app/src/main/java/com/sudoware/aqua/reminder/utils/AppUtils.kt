package com.sudoware.aqua.reminder.utils

import java.text.SimpleDateFormat
import java.util.*


class AppUtils {
    companion object {
        fun calculateIntake(weight: Int, workTime: Int): Double {
            return ((weight * 100 / 3.0) + (workTime / 6 * 7))
        }

        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }

        const val USERS_SHARED_PREF = "user_pref"
        const val PRIVATE_MODE = 0
        const val NAME_KEY = "name"
        const val GENDER_KEY = "gender"
        const val WEIGHT_KEY = "weight"
        const val WORK_TIME_KEY = "worktime"
        const val TOTAL_INTAKE = "totalintake"
        const val NOTIFICATION_STATUS_KEY = "notificationstatus"
        const val NOTIFICATION_FREQUENCY_KEY = "notificationfrequency"
        const val NOTIFICATION_MSG_KEY = "notificationmsg"
        const val SLEEPING_TIME_KEY = "sleepingtime"
        const val WAKEUP_TIME = "wakeuptime"
        const val NOTIFICATION_TONE_URI_KEY = "notificationtone"
        const val FIRST_RUN_KEY = "firstrun"
        const val THEME_SELECTION = "themeselection"
        const val MINUTES_30 = 30
        const val MINUTES_45 = 45
        const val MINUTES_60 = 60
    }
}