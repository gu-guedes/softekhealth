package com.example.softekhealth.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Receiver para reiniciar os alarmes de notificação quando o dispositivo é reiniciado
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var notificationHelper: NotificationHelper
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reagendar todas as notificações após o boot do dispositivo
            notificationHelper.scheduleDailyMoodReminder()
            notificationHelper.scheduleWeeklyQuestionnaireReminder()
        }
    }
}
