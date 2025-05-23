package com.example.softekhealth

import android.app.Application
import com.example.softekhealth.util.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MindCompassApplication : Application() {
    
    @Inject
    lateinit var appInitializer: AppInitializer
    
    override fun onCreate() {
        super.onCreate()
        // Inicializar componentes após a injeção de dependência
        appInitializer.initialize()
    }
}
