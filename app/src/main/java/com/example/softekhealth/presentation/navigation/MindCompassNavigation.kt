package com.example.softekhealth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.softekhealth.presentation.forms.FormsScreen
import com.example.softekhealth.presentation.forms.FormsViewModel
import com.example.softekhealth.presentation.home.HomeScreen
import com.example.softekhealth.presentation.home.HomeViewModel
import com.example.softekhealth.presentation.login.LoginScreen
import com.example.softekhealth.presentation.login.LoginViewModel

@Composable
fun MindCompassNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                onNavigateToForms = {
                    navController.navigate(Screen.Forms.route)
                },
                viewModel = viewModel
            )
        }
        composable(Screen.Forms.route) {
            val viewModel = hiltViewModel<FormsViewModel>()
            FormsScreen(
                onFormComplete = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
