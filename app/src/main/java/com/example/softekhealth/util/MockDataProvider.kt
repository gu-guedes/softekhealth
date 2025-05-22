package com.example.softekhealth.util

import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.model.MoodType
import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.model.SymptomFrequency
import com.example.softekhealth.presentation.home.WellnessTip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

/**
 * Classe auxiliar que fornece dados simulados para testes durante o desenvolvimento.
 * Será substituída pela implementação real com backend na Sprint 2.
 */
object MockDataProvider {

    /**
     * Retorna um fluxo de humores simulados para testes da UI
     */
    fun getMockMoods(userEmail: String): Flow<List<Mood>> {
        return flowOf(generateMockMoods(userEmail))
    }
    
    /**
     * Retorna um fluxo de questionários simulados para testes da UI
     */
    fun getMockQuestionnaires(userEmail: String): Flow<List<Questionnaire>> {
        return flowOf(generateMockQuestionnaires(userEmail))
    }
    
    /**
     * Retorna um fluxo simulando um questionário salvo com sucesso
     */
    fun saveMockQuestionnaire(questionnaire: Questionnaire): Flow<Result<Long>> {
        return flowOf(Result.success(Random.nextLong(1, 1000)))
    }
    
    /**
     * Gera uma lista de humores simulados para os últimos 14 dias
     */
    private fun generateMockMoods(userEmail: String): List<Mood> {
        val moods = mutableListOf<Mood>()
        val calendar = Calendar.getInstance()
        
        // Voltar 14 dias para começar a gerar dados
        calendar.add(Calendar.DAY_OF_YEAR, -13)
        
        for (i in 0 until 14) {
            // Alguns dias podem não ter registro (simulando registros reais)
            if (Random.nextInt(0, 10) < 8) { // 80% de chance de ter registro
                val moodType = when(Random.nextInt(0, 9)) {
                    0 -> MoodType.HAPPY
                    1 -> MoodType.CALM
                    2 -> MoodType.NEUTRAL
                    3 -> MoodType.SAD
                    4 -> MoodType.ANGRY
                    5 -> MoodType.STRESSED
                    6 -> MoodType.ANXIOUS
                    7 -> MoodType.TIRED
                    else -> MoodType.ENERGETIC
                }
                
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
     * Gera uma lista de questionários simulados
     */
    private fun generateMockQuestionnaires(userEmail: String): List<Questionnaire> {
        val questionnaires = mutableListOf<Questionnaire>()
        val calendar = Calendar.getInstance()
        
        // Voltar 30 dias para começar a gerar dados
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        
        for (i in 0 until 5) {
            val date = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, Random.nextInt(3, 8)) // Intervalo entre questionários
            
            val symptoms = listOf(
                "Dificuldade para dormir",
                "Dores de cabeça",
                "Tensão muscular",
                "Fadiga excessiva",
                "Dificuldade de concentração"
            ).shuffled().take(Random.nextInt(1, 4))
            
            val frequencies = symptoms.associateWith {
                SymptomFrequency.values()[Random.nextInt(0, SymptomFrequency.values().size)]
            }
            
            val questionnaire = Questionnaire(
                id = i.toLong(),
                userEmail = userEmail,
                date = date,
                stressLevel = Random.nextInt(0, 11),
                selectedSymptoms = symptoms,
                symptomFrequency = frequencies,
                notes = if (Random.nextBoolean()) "Observações da semana ${i+1}" else null,
                isDraft = false
            )
            
            questionnaires.add(questionnaire)
        }
        
        return questionnaires
    }
    
    /**
     * Gera uma nota aleatória com base no tipo de humor
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
    
    /**
     * Retorna dicas de bem-estar simuladas
     */
    fun getMockWellnessTips(): List<WellnessTip> {
        return listOf(
            WellnessTip(
                id = 1,
                title = "Respiração consciente",
                shortDescription = "5 minutos de respiração profunda podem reduzir significativamente o estresse.",
                fullContent = "Técnica de respiração consciente: Inspire lentamente pelo nariz contando até 4, segure por 2, expire pela boca contando até 6. Repita por 5 minutos."
            ),
            WellnessTip(
                id = 2,
                title = "Pausas ativas",
                shortDescription = "Levante-se e faça uma pausa a cada 60 minutos de trabalho.",
                fullContent = "Dicas para pausas ativas: Alongue-se, caminhe por 5 minutos, faça exercícios de rotação do pescoço e ombros, olhe para um ponto distante para relaxar os olhos."
            ),
            WellnessTip(
                id = 3,
                title = "Hidratação",
                shortDescription = "Mantenha-se hidratado para melhorar a concentração e energia.",
                fullContent = "A desidratação, mesmo leve, pode afetar sua concentração e energia. Mantenha uma garrafa de água por perto e estabeleça metas de consumo diário. Experimente adicionar frutas para um sabor natural."
            ),
            WellnessTip(
                id = 4,
                title = "Mindfulness",
                shortDescription = "Pratique atenção plena para reduzir a ansiedade no trabalho.",
                fullContent = "Exercício rápido de mindfulness: Observe 5 coisas que você pode ver, 4 que pode tocar, 3 que pode ouvir, 2 que pode cheirar e 1 que pode provar. Este exercício ajuda a ancorar você no momento presente."
            ),
            WellnessTip(
                id = 5,
                title = "Conexão social",
                shortDescription = "Cultive relacionamentos positivos com os colegas de trabalho.",
                fullContent = "Dicas para conexões sociais no trabalho: Reserve tempo para almoçar com colegas, participe de eventos da empresa, crie um grupo de interesses comuns, ofereça ajuda quando possível e pratique a escuta ativa."
            )
        )
    }
}
