package com.example.softekhealth.util

import android.content.Context
import com.example.softekhealth.data.local.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Classe responsu00e1vel por inicializar componentes do aplicativo no startup
 */
@Singleton
class AppInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val notificationHelper: NotificationHelper
) {

    /**
     * Inicializa os componentes do aplicativo
     */
    fun initialize() {
        // Configurar notificau00e7u00f5es se o usuu00e1rio estiver logado
        if (sessionManager.isLoggedIn()) {
            setupNotifications()
        }
    }

    /**
     * Configura as notificau00e7u00f5es do aplicativo
     */
    fun setupNotifications() {
        // Agendar notificau00e7u00e3o diu00e1ria para registro de humor (20h)
        notificationHelper.scheduleDailyMoodReminder(hourOfDay = 20, minute = 0)
        
        // Agendar notificau00e7u00e3o semanal para questionu00e1rio (sexta-feira u00e0s 15h)
        notificationHelper.scheduleWeeklyQuestionnaireReminder()
    }
    
    /**
     * Cancela todas as notificau00e7u00f5es ao fazer logout
     */
    fun cleanupOnLogout() {
        notificationHelper.cancelAllReminders()
    }
}
