package com.example.levelupmobile.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.example.levelupmobile.session.SessionManager
import com.example.levelupmobile.remote.LoginResponse
import com.example.levelupmobile.remote.UserPayload
import com.example.levelupmobile.repository.AuthRepository
import com.example.levelupmobile.utils.Result
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BehaviorSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    Given("Un LoginViewModel") {
        // Mocks relajados
        val application = mockk<Application>(relaxed = true)
        val repository = mockk<AuthRepository>()
        val sessionManager = mockk<SessionManager>(relaxed = true)

        // Instancia del ViewModel
        val viewModel = LoginViewModel(application, repository, sessionManager)

        When("El login es exitoso") {
            val email = "test@test.com"
            val pass = "123456"
            val mockResponse = LoginResponse("token123", UserPayload("USER", "Test", email))

            // Preparamos el estado inicial
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(pass)

            // Mockeamos la respuesta
            coEvery { repository.loginUser(email, pass) } returns Result.Success(mockResponse)

            Then("Debe guardar sesión y navegar a home") {
                runTest {
                    // Escuchamos el evento de navegación (SharedFlow)
                    viewModel.navigateToHome.test {
                        // Ejecutamos la acción
                        viewModel.onLoginClicked()

                        // Avanzamos el tiempo hasta que las corrutinas terminen
                        testDispatcher.scheduler.advanceUntilIdle()

                        // verificamos que se emitió 'true'
                        awaitItem() shouldBe true
                    }
                }

                // Verificamos que se llamó al SessionManager
                coVerify { sessionManager.saveLoginSession("token123", email) }

                // Verificamos el estado final (ya no debe estar cargando)
                viewModel.uiState.value.isLoading shouldBe false
                viewModel.uiState.value.loginError shouldBe null
            }
        }

        When("El login falla") {
            val email = "fail@test.com"
            val pass = "wrong"
            val errorMsg = "Error de prueba"

            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(pass)

            coEvery { repository.loginUser(email, pass) } returns Result.Error(Exception(errorMsg))

            Then("Debe mostrar el error y NO guardar sesión") {
                runTest {
                    // no esperamos navegación, así que no usamos Turbine aquí
                    // solo ejecutamos y verificamos el estado final
                    viewModel.onLoginClicked()

                    testDispatcher.scheduler.advanceUntilIdle()
                }

                // Verificamos estado final de error
                viewModel.uiState.value.isLoading shouldBe false
                viewModel.uiState.value.loginError shouldBe errorMsg

                // Verificamos que NO se guardó sesión
                coVerify(exactly = 0) { sessionManager.saveLoginSession(any(), any()) }
            }
        }
    }
})