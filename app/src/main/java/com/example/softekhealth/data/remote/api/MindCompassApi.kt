package com.example.softekhealth.data.remote.api

// Importau00e7u00f5es comentadas para resolver problema de compilau00e7u00e3o
//import com.example.softekhealth.data.remote.dto.MoodDto
//import com.example.softekhealth.data.remote.dto.QuestionnaireDto
//import com.example.softekhealth.data.remote.dto.UserDto
//import com.example.softekhealth.data.remote.dto.WellnessTipDto
//import retrofit2.Response
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.POST
//import retrofit2.http.Path
//import retrofit2.http.Query

/**
 * Interface de API para o backend do Mind Compass
 * Preparada para futura implementau00e7u00e3o na segunda sprint
 * 
 * NOTA: Esta interface foi temporariamente comentada para resolver problemas de compilau00e7u00e3o.
 * Seru00e1 implementada corretamente na segunda sprint conforme planejado.
 */
interface MindCompassApi {
    // Interface vazia para compilau00e7u00e3o inicial
    // A implementau00e7u00e3o completa seru00e1 feita na Sprint 2
    
    /*
    // Autenticau00e7u00e3o
    @POST("auth/login")
    suspend fun login(@Body email: String): Response<UserDto>
    
    // Gerenciamento de humor
    @POST("moods")
    suspend fun saveMood(@Body mood: MoodDto): Response<Long>
    
    @GET("moods")
    suspend fun getMoods(@Query("userEmail") userEmail: String): Response<List<MoodDto>>
    
    @GET("moods/recent")
    suspend fun getRecentMoods(
        @Query("userEmail") userEmail: String,
        @Query("limit") limit: Int
    ): Response<List<MoodDto>>
    
    @GET("moods/range")
    suspend fun getMoodsByDateRange(
        @Query("userEmail") userEmail: String,
        @Query("startDate") startDate: Long,
        @Query("endDate") endDate: Long
    ): Response<List<MoodDto>>
    
    // Gerenciamento de questionu00e1rios
    @POST("questionnaires")
    suspend fun saveQuestionnaire(@Body questionnaire: QuestionnaireDto): Response<Long>
    
    @GET("questionnaires/{id}")
    suspend fun getQuestionnaireById(@Path("id") id: Long): Response<QuestionnaireDto>
    
    @GET("questionnaires")
    suspend fun getQuestionnaires(@Query("userEmail") userEmail: String): Response<List<QuestionnaireDto>>
    
    @GET("questionnaires/range")
    suspend fun getQuestionnairesByDateRange(
        @Query("userEmail") userEmail: String,
        @Query("startDate") startDate: Long,
        @Query("endDate") endDate: Long
    ): Response<List<QuestionnaireDto>>
    
    // Dicas de bem-estar
    @GET("wellness/tips")
    suspend fun getWellnessTips(): Response<List<WellnessTipDto>>
    */
}
