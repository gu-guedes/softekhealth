package com.example.softekhealth.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softekhealth.domain.model.Mood
import com.example.softekhealth.domain.model.MoodType
import com.example.softekhealth.domain.usecase.GetRecentMoodsUseCase
import com.example.softekhealth.domain.usecase.SaveMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentMoodsUseCase: GetRecentMoodsUseCase,
    private val saveMoodUseCase: SaveMoodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _recentMoods = getRecentMoodsUseCase("user@softtek.com", 14) // Hardcoded for now

    val state: StateFlow<HomeState> = combine(_state, _recentMoods) { state, moods ->
        state.copy(
            recentMoods = moods,
            todayMood = moods.firstOrNull { 
                val today = Date()
                val moodDate = it.date
                moodDate.day == today.day && moodDate.month == today.month && moodDate.year == today.year
            }?.moodType
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectMood -> {
                viewModelScope.launch {
                    val mood = Mood(
                        userEmail = "user@softtek.com", // Hardcoded for now
                        moodType = event.moodType,
                        date = Date()
                    )
                    saveMoodUseCase(mood)
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    // Funções de utilidade para cálculos de estatísticas de bem-estar
    fun calculateStressLevel(): Int {
        // Lógica para calcular nível de estresse com base nos dados recentes
        val recentMoods = state.value.recentMoods
        if (recentMoods.isEmpty()) return 0
        
        val stressScore = recentMoods.sumOf { mood ->
            when (mood.moodType) {
                MoodType.STRESSED, MoodType.ANGRY -> 2
                MoodType.ANXIOUS, MoodType.SAD -> 1
                MoodType.NEUTRAL -> 0
                MoodType.HAPPY, MoodType.ENERGETIC, MoodType.CALM -> -1
                else -> -1 // Para qualquer tipo de humor não especificado
            }.toInt()
        }
        
        return ((stressScore * 10) / (recentMoods.size * 2)).coerceIn(0, 10)
    }
    
    fun calculateMoodScore(): Int {
        // Lógica para calcular pontuação de humor com base nos dados recentes
        val recentMoods = state.value.recentMoods
        if (recentMoods.isEmpty()) return 50
        
        val moodScore = recentMoods.sumOf { mood ->
            when (mood.moodType) {
                MoodType.HAPPY, MoodType.ENERGETIC -> 2
                MoodType.CALM -> 1
                MoodType.NEUTRAL -> 0
                MoodType.SAD, MoodType.TIRED -> -1
                MoodType.STRESSED, MoodType.ANXIOUS, MoodType.ANGRY -> -2
                else -> -2 // Para qualquer tipo de humor não especificado
            }.toInt()
        }
        
        return (50 + ((moodScore * 50) / (recentMoods.size * 2))).coerceIn(0, 100)
    }
    
    fun calculateConsistencyScore(): Int {
        // Lógica para calcular consistência de registro de humor
        val recentMoods = state.value.recentMoods
        if (recentMoods.isEmpty()) return 0
        
        // Quantidade de dias com registros nos últimos 14 dias
        val uniqueDays = recentMoods.map { mood ->
            val date = mood.date
            "${date.year}-${date.month}-${date.day}"
        }.toSet().size
        
        return (uniqueDays * 100 / 14).coerceIn(0, 100)
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val recentMoods: List<Mood> = emptyList(),
    val todayMood: MoodType? = null,
    val error: String? = null
)

sealed class HomeEvent {
    data class SelectMood(val moodType: MoodType) : HomeEvent()
}
