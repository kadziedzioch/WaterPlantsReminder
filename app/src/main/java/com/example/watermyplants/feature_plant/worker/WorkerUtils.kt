package com.example.watermyplants.feature_plant.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.watermyplants.R
import com.example.watermyplants.feature_plant.presentation.MainActivity

fun makePlantReminderNotification(
    message: String,
    context: Context
) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        CHANNEL_ID,
        VERBOSE_NOTIFICATION_CHANNEL_NAME,
        importance
    )
    channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_local_florist_24)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)


    if(Build.VERSION.SDK_INT >= 33){
        if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED){
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }
    }
    else{
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    flags = flags or PendingIntent.FLAG_IMMUTABLE

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}


