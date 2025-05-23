package com.example.softekhealth.presentation.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.model.SymptomFrequency
import com.example.softekhealth.domain.usecase.SaveQuestionnaireUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FormsViewModel @Inject constructor(
    private val saveQuestionnaireUseCase: SaveQuestionnaireUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FormsState())
    val state: StateFlow<FormsState> = _state.asStateFlow()

    fun onEvent(event: FormsEvent) {
        when (event) {
            is FormsEvent.StressLevelChanged -> {
                _state.update { it.copy(stressLevel = event.level) }
            }
            is FormsEvent.SymptomCheckedChanged -> {
                val symptoms = _state.value.selectedSymptoms.toMutableList()
                if (event.isChecked) {
                    if (!symptoms.contains(event.symptom)) {
                        symptoms.add(event.symptom)
                    }
                } else {
                    symptoms.remove(event.symptom)
                }
                _state.update { it.copy(selectedSymptoms = symptoms) }
            }
            is FormsEvent.SymptomFrequencyChanged -> {
                val frequencyMap = _state.value.symptomFrequency.toMutableMap()
                frequencyMap[event.symptom] = event.frequency
                _state.update { it.copy(symptomFrequency = frequencyMap) }
            }
            is FormsEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.notes) }
            }
            is FormsEvent.NextPage -> {
                if (_state.value.currentPage < _state.value.totalPages - 1) {
                    _state.update { it.copy(currentPage = it.currentPage + 1) }
                }
            }
            is FormsEvent.PreviousPage -> {
                if (_state.value.currentPage > 0) {
                    _state.update { it.copy(currentPage = it.currentPage - 1) }
                }
            }
            is FormsEvent.SaveDraft -> {
                saveDraft()
            }
            is FormsEvent.Submit -> {
                submitQuestionnaire()
            }
            is FormsEvent.GoToPage -> {
                if (event.page in 0 until _state.value.totalPages) {
                    _state.update { it.copy(currentPage = event.page) }
                }
            }
        }
    }

    private fun saveDraft() {
        _state.update { it.copy(isSaving = true, error = null) }
        viewModelScope.launch {
            val questionnaire = createQuestionnaire(isDraft = true)
            val result = saveQuestionnaireUseCase(questionnaire)
            result.fold(
                onSuccess = { id ->
                    _state.update { it.copy(isSaving = false, isSuccess = true, draftId = id) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isSaving = false, error = error.message) }
                }
            )
        }
    }

    private fun submitQuestionnaire() {
        _state.update { it.copy(isSubmitting = true, error = null) }
        viewModelScope.launch {
            val questionnaire = createQuestionnaire(isDraft = false)
            val result = saveQuestionnaireUseCase(questionnaire)
            result.fold(
                onSuccess = { _ ->
                    _state.update { it.copy(isSubmitting = false, isCompleted = true) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isSubmitting = false, error = error.message) }
                }
            )
        }
    }

    private fun createQuestionnaire(isDraft: Boolean): Questionnaire {
        return Questionnaire(
            id = _state.value.draftId ?: 0,
            userEmail = "user@softtek.com", // Hardcoded for now
            date = Date(),
            stressLevel = _state.value.stressLevel,
            selectedSymptoms = _state.value.selectedSymptoms,
            symptomFrequency = _state.value.symptomFrequency,
            notes = _state.value.notes,
            isDraft = isDraft
        )
    }
}

data class FormsState(
    val currentPage: Int = 0,
    val totalPages: Int = 4,
    val stressLevel: Int = 5,
    val selectedSymptoms: List<String> = emptyList(),
    val symptomFrequency: Map<String, SymptomFrequency> = emptyMap(),
    val notes: String = "",
    val isSubmitting: Boolean = false,
    val isSaving: Boolean = false,
    val isCompleted: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val draftId: Long? = null
)

sealed class FormsEvent {
    data class StressLevelChanged(val level: Int) : FormsEvent()
    data class SymptomCheckedChanged(val symptom: String, val isChecked: Boolean) : FormsEvent()
    data class SymptomFrequencyChanged(val symptom: String, val frequency: SymptomFrequency) : FormsEvent()
    data class NotesChanged(val notes: String) : FormsEvent()
    data class GoToPage(val page: Int) : FormsEvent()
    object NextPage : FormsEvent()
    object PreviousPage : FormsEvent()
    object SaveDraft : FormsEvent()
    object Submit : FormsEvent()
}
