package com.example.softekhealth.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Forms : Screen("forms_screen")
    
    companion object {
        fun fromRoute(route: String): Screen {
            return when(route) {
                Login.route -> Login
                Home.route -> Home
                Forms.route -> Forms
                else -> Home
            }
        }
    }
}
