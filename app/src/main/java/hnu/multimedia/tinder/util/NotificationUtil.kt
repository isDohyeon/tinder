package hnu.multimedia.tinder.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import hnu.multimedia.tinder.R

class NotificationUtil {

    companion object {
        private val CHANNEL_ID = "channelId"
        private val CHANNEL_NAME = "channelName"
        private val CHANNEL_DESC = "channelDesc"

        fun createNotification(context: Context, title: String, message: String) {
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder : NotificationCompat.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                mChannel.description = CHANNEL_DESC
                notificationManager.createNotificationChannel(mChannel)
                builder = NotificationCompat.Builder(context, CHANNEL_ID)
            } else {
                builder = NotificationCompat.Builder(context)
            }
            builder.setSmallIcon(R.drawable.logo_yellow)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.setContentTitle(title)
            builder.setContentText(message)

            notificationManager.notify(1, builder.build())
        }

        fun requestPermissions(activity: AppCompatActivity) {
            val isTiramisuOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            val permission = Manifest.permission.POST_NOTIFICATIONS

            var hasPermission =
                if (isTiramisuOrHigher) {
                    ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }
            val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                hasPermission = it
            }

            if (!hasPermission) {
                launcher.launch(permission)
            }
        }
    }
}