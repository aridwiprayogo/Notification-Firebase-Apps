package com.foodinger.notificationfirebaseapps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.edit().putString(Constant.FIREBASE_TOKEN, token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d(TAG, "onMessageReceived: ${remoteMessage.from}")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannel()
        }

        if (remoteMessage.data.isNotEmpty()) {
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        val notification = remoteMessage.notification

        Log.d(TAG, "onMessageReceived: ${notification?.body}")
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(notification?.title)
            setContentText(notification?.body)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannel() {
        val channelName = "anu"
        val channelDescription = "anu description"
        val channel: NotificationChannel
        channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = channelDescription
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleJob() {
        TODO("Not yet implemented")
    }

    private fun handleNow() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CHANNEL_ID: String = "coba notification"
        private val TAG: String = NotificationService::class.java.simpleName
    }
}
