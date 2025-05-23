package com.example.softekhealth.util

/**
 * Constantes utilizadas em todo o aplicativo
 */
object Constants {
    // Padrões para validação
    const val EMAIL_PATTERN = "^[\\w.-]+@softtek\\.com$"
    
    // Limites para campos numéricos
    const val MIN_STRESS_LEVEL = 0
    const val MAX_STRESS_LEVEL = 10
    
    // Configurações de UI
    const val MOOD_HISTORY_DAYS = 14
    const val RECENT_QUESTIONNAIRES_LIMIT = 5
    
    // Configurações de tempo
    const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
    const val ONE_WEEK_IN_MILLIS = 7 * ONE_DAY_IN_MILLIS
    
    // Categorias de dicas de bem-estar
    object WellnessCategories {
        const val MENTAL = "mental"
        const val PHYSICAL = "physical"
        const val SOCIAL = "social"
        const val WORK_LIFE = "work_life"
        const val RELAXATION = "relaxation"
    }
    
    // Nomes de eventos para Analytics (para implementação futura)
    object AnalyticsEvents {
        const val MOOD_SELECTED = "mood_selected"
        const val QUESTIONNAIRE_COMPLETED = "questionnaire_completed"
        const val QUESTIONNAIRE_SAVED_DRAFT = "questionnaire_saved_draft"
        const val WELLNESS_TIP_VIEWED = "wellness_tip_viewed"
    }
}
