package xyz.z3ro.colorenhance.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.ui.activity.MainActivity
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.Operations
import xyz.z3ro.colorenhance.utility.PreferenceHelper
import java.io.File

class BootCompletedReceiver : BroadcastReceiver() {

    private val TAG = "BootCompletedReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_BOOT_COMPLETED) {

            if (PreferenceHelper.getBoolean(context, Constants.APPLY_ON_BOOT, false) && PreferenceHelper.getBoolean(
                    context,
                    Constants.ENABLED_STATUS,
                    false
                )
            ) {
                applying(context!!)
                Operations.restoreBackground(
                    File(PreferenceHelper.getString(context, Constants.TEMP_FOLDER, ""), "Current Preset").absolutePath
                )
            }
        } else throw NullPointerException()
    }

    private fun applying(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_colorize_blue_24dp)
            .setContentTitle("Color Enhance")
            .setContentText("Applied selected preset")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(778, notificationBuilder.build())
        }
    }
}