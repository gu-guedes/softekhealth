package com.example.softekhealth.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Receiver para lembretes diários de registro de humor
 */
@AndroidEntryPoint
class MoodReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationHelper.showMoodReminderNotification()
        
        // Reagendar para o próximo dia
        notificationHelper.scheduleDailyMoodReminder()
    }
}

/**
 * Receiver para lembretes semanais de questionário
 */
@AndroidEntryPoint
class QuestionnaireReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationHelper.showQuestionnaireReminderNotification()
        
        // Reagendar para a próxima semana
        notificationHelper.scheduleWeeklyQuestionnaireReminder()
    }
}
