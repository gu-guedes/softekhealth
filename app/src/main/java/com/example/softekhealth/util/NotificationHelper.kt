package com.example.softekhealth.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.softekhealth.MainActivity
import com.example.softekhealth.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Classe auxiliar para gerenciar notificações locais no aplicativo
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    /**
     * Cria os canais de notificação necessários (obrigatório para Android 8.0+)
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Canal para lembretes diários
            val dailyRemindersChannel = NotificationChannel(
                CHANNEL_DAILY_REMINDERS,
                "Lembretes Diários",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Lembretes diários para registrar seu humor e bem-estar"
                enableLights(true)
            }

            // Canal para lembretes de questionários
            val questionnaireRemindersChannel = NotificationChannel(
                CHANNEL_QUESTIONNAIRE_REMINDERS,
                "Lembretes de Questionários",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Lembretes para completar questionários de autoavaliação"
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.createNotificationChannels(
                listOf(dailyRemindersChannel, questionnaireRemindersChannel)
            )
        }
    }

    /**
     * Agenda uma notificação diária para registrar o humor
     */
    fun scheduleDailyMoodReminder(hourOfDay: Int = 20, minute: Int = 0) {
        val intent = Intent(context, MoodReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_MOOD_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // Se o horário já passou hoje, agendar para amanhã
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Exibe uma notificação de lembrete de humor
     */
    fun showMoodReminderNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_REMINDERS)
            .setSmallIcon(R.drawable.mindcompass_logo)
            .setContentTitle("Registre seu humor")
            .setContentText("Como você está se sentindo hoje?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(DAILY_MOOD_NOTIFICATION_ID, notification)
    }

    /**
     * Agenda uma notificação semanal para o questionário de autoavaliação
     */
    fun scheduleWeeklyQuestionnaireReminder(dayOfWeek: Int = Calendar.FRIDAY, hourOfDay: Int = 15, minute: Int = 0) {
        val intent = Intent(context, QuestionnaireReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            WEEKLY_QUESTIONNAIRE_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // Se o horário já passou esta semana, agendar para a próxima semana
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Exibe uma notificação de lembrete para o questionário
     */
    fun showQuestionnaireReminderNotification() {
        val intent = Intent(context, MainActivity::class.java) // Adicionar dados extras para abrir tela específica
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_QUESTIONNAIRE_REMINDERS)
            .setSmallIcon(R.drawable.mindcompass_logo)
            .setContentTitle("Avaliação Semanal")
            .setContentText("É hora de realizar sua autoavaliação psicossocial semanal")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(WEEKLY_QUESTIONNAIRE_NOTIFICATION_ID, notification)
    }

    /**
     * Cancela todos os lembretes agendados
     */
    fun cancelAllReminders() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Cancelar lembrete diário
        val dailyIntent = Intent(context, MoodReminderReceiver::class.java)
        val dailyPendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_MOOD_REMINDER_REQUEST_CODE,
            dailyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(dailyPendingIntent)
        
        // Cancelar lembrete semanal
        val weeklyIntent = Intent(context, QuestionnaireReminderReceiver::class.java)
        val weeklyPendingIntent = PendingIntent.getBroadcast(
            context,
            WEEKLY_QUESTIONNAIRE_REMINDER_REQUEST_CODE,
            weeklyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(weeklyPendingIntent)
    }

    companion object {
        // IDs dos canais de notificação
        const val CHANNEL_DAILY_REMINDERS = "daily_reminders"
        const val CHANNEL_QUESTIONNAIRE_REMINDERS = "questionnaire_reminders"
        
        // IDs das notificações
        const val DAILY_MOOD_NOTIFICATION_ID = 1001
        const val WEEKLY_QUESTIONNAIRE_NOTIFICATION_ID = 1002
        
        // Códigos de requisição para PendingIntents
        const val DAILY_MOOD_REMINDER_REQUEST_CODE = 2001
        const val WEEKLY_QUESTIONNAIRE_REMINDER_REQUEST_CODE = 2002
    }
}
