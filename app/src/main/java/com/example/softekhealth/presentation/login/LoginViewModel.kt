package com.example.softekhealth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softekhealth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = loginUseCase(_state.value.email)
            result.fold(
                onSuccess = { user ->
                    _state.update { it.copy(isLoading = false, isLoggedIn = true, user = user) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}

data class LoginState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val user: Any? = null
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    object Login : LoginEvent()
}
