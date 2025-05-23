package com.example.softekhealth.domain.usecase

import com.example.softekhealth.domain.model.Questionnaire
import com.example.softekhealth.domain.repository.QuestionnaireRepository
import javax.inject.Inject

class SaveQuestionnaireUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository
) {
    suspend operator fun invoke(questionnaire: Questionnaire): Result<Long> {
        if (questionnaire.userEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Email do usuário não pode estar em branco"))
        }
        
        if (questionnaire.stressLevel < 0 || questionnaire.stressLevel > 10) {
            return Result.failure(IllegalArgumentException("Nível de estresse deve estar entre 0 e 10"))
        }
        
        return questionnaireRepository.saveQuestionnaire(questionnaire)
    }
}
