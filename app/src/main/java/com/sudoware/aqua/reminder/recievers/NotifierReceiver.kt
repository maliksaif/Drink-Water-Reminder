package com.sudoware.aqua.reminder.recievers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.sudoware.aqua.reminder.R
import com.sudoware.aqua.reminder.helpers.NotificationHelper
import com.sudoware.aqua.reminder.utils.AppUtils

class NotifierReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val prefs = context.getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        val notificationsTone = prefs.getString(
            AppUtils.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            ).toString()
        )

        val title = context.resources.getString(R.string.app_name)
        val messageToShow = prefs.getString(
            AppUtils.NOTIFICATION_MSG_KEY,
            context.resources.getString(R.string.pref_notification_message_value)
        )

        /* Notify */
        val nHelper = NotificationHelper(context)
        @SuppressLint("ResourceType") val nBuilder = nHelper
            .getNotification(title, messageToShow, notificationsTone)
        nHelper.notify(1, nBuilder)

    }
}