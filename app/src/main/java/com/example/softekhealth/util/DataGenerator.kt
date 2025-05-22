package com.example.softekhealth.util

import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.model.MoodType
import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.model.SymptomFrequency
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

/**
 * Classe utilitária para gerar dados simulados para testes e demonstração
 */
object DataGenerator {

    /**
     * Gera um histórico de humor para os últimos 14 dias
     * @param userEmail Email do usuário
     * @return Lista de Mood com dados simulados
     */
    fun generateMoodHistory(userEmail: String): List<Mood> {
        val moods = mutableListOf<Mood>()
        val calendar = Calendar.getInstance()
        val today = calendar.time
        
        // Voltar 14 dias e gerar dados
        calendar.add(Calendar.DAY_OF_YEAR, -14)
        
        for (i in 0 until 14) {
            // Alguns dias podem não ter registro
            if (Random.nextInt(0, 10) < 8) { // 80% de chance de ter registro
                val moodType = MoodType.values()[Random.nextInt(MoodType.values().size)]
                val mood = Mood(
                    id = i.toLong(),
                    userEmail = userEmail,
                    moodType = moodType,
                    date = calendar.time,
                    note = if (Random.nextBoolean()) getRandomNote(moodType) else null
                )
                moods.add(mood)
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        return moods
    }

    /**
     * Gera um histórico de questionários respondidos
     * @param userEmail Email do usuário
     * @return Lista de Questionnaire com dados simulados
     */
    fun generateQuestionnaireHistory(userEmail: String): List<Questionnaire> {
        val questionnaires = mutableListOf<Questionnaire>()
        val calendar = Calendar.getInstance()
        val today = calendar.time
        
        // Gerar 5 questionários nos últimos 30 dias
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        
        for (i in 0 until 5) {
            val date = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, Random.nextInt(3, 8)) // Intervalo entre questionários
            
            val questionnaire = Questionnaire(
                id = i.toLong(),
                userEmail = userEmail,
                date = date,
                stressLevel = Random.nextInt(0, 11),
                selectedSymptoms = generateRandomSymptoms(),
                symptomFrequency = generateRandomSymptomFrequencies(generateRandomSymptoms()),
                notes = if (Random.nextBoolean()) "Anotações da semana ${i+1}" else null,
                isDraft = false
            )
            
            questionnaires.add(questionnaire)
        }
        
        return questionnaires
    }
    
    /**
     * Gera uma lista aleatória de sintomas
     * @return Lista de sintomas selecionados aleatoriamente
     */
    private fun generateRandomSymptoms(): List<String> {
        val allSymptoms = listOf(
            "Dificuldade para dormir",
            "Dores de cabeça",
            "Tensão muscular",
            "Fadiga excessiva",
            "Dificuldade de concentração",
            "Irritabilidade",
            "Ansiedade",
            "Mudanças de apetite",
            "Sentimentos de tristeza",
            "Preocupação constante",
            "Dores no corpo",
            "Palpitações",
            "Pensamentos acelerados",
            "Indecisão",
            "Procrastinação"
        )
        
        val selectedCount = Random.nextInt(0, 6) // De 0 a 5 sintomas
        return allSymptoms.shuffled().take(selectedCount)
    }
    
    /**
     * Gera frequências aleatórias para os sintomas
     * @param symptoms Lista de sintomas para gerar frequências
     * @return Mapa de sintoma para frequência
     */
    private fun generateRandomSymptomFrequencies(symptoms: List<String>): Map<String, SymptomFrequency> {
        val frequencies = mutableMapOf<String, SymptomFrequency>()
        
        symptoms.forEach { symptom ->
            frequencies[symptom] = SymptomFrequency.values()[Random.nextInt(SymptomFrequency.values().size)]
        }
        
        return frequencies
    }
    
    /**
     * Gera uma nota aleatória com base no tipo de humor
     * @param moodType Tipo de humor
     * @return Nota aleatória
     */
    private fun getRandomNote(moodType: MoodType): String {
        return when (moodType) {
            MoodType.HAPPY -> "Tive um dia muito produtivo hoje!"
            MoodType.ENERGETIC -> "Muita energia para completar tarefas!"
            MoodType.CALM -> "Dia tranquilo, consegui manter o foco."
            MoodType.NEUTRAL -> "Dia comum, sem grandes altos ou baixos."
            MoodType.SAD -> "Senti-me um pouco desanimado hoje."
            MoodType.TIRED -> "Muitas reuniões hoje, estou exausto."
            MoodType.STRESSED -> "Prazo apertado no projeto principal."
            MoodType.ANXIOUS -> "Preocupado com a apresentação de amanhã."
            MoodType.ANGRY -> "Frustrações com alguns bloqueios no trabalho."
        }
    }
}
