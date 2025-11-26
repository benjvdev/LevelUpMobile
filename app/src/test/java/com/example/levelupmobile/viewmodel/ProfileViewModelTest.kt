package com.example.levelupmobile.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.example.levelupmobile.session.SessionManager
import com.example.levelupmobile.remote.User
import com.example.levelupmobile.repository.AuthRepository
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
class ProfileViewModelTest : BehaviorSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    //mocks relajados
    val application = mockk<Application>(relaxed = true)
    val repository = mockk<AuthRepository>()
    val sessionManager = mockk<SessionManager>(relaxed = true)

    Given("Un ProfileViewModel") {

        When("Se inicia y la carga del perfil es EXITOSA") {
            val mockUser = User(
                id = 1,
                nombres = "Benja",
                apellidos = "Dev",
                email = "benja@test.com",
                rol = "USER",
                fechaNacimiento = "1999-01-01",
                direccion = "Calle Falsa 123"
            )

            coEvery { repository.getProfile() } returns mockUser

            val viewModel = ProfileViewModel(application, repository, sessionManager)

            Then("El estado debe actualizarse con los datos del usuario") {
                runTest {
                    testDispatcher.scheduler.advanceUntilIdle()
                    viewModel.user.value shouldBe mockUser
                }
            }
        }

        When("Se inicia y la carga del perfil FALLA (Token inválido)") {
            //aquí sí queremos que devuelva null para probar el logout automático
            coEvery { repository.getProfile() } returns null

            val viewModel = ProfileViewModel(application, repository, sessionManager)

            Then("Debe hacer logout automático y navegar al login") {
                runTest {
                    viewModel.navigateToLogin.test {
                        testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe true
                    }
                }
                coVerify { sessionManager.clearLoginState() }
                viewModel.user.value shouldBe null
            }
        }

        When("El usuario presiona el botón Cerrar Sesión") {
            //devolvemos un usuario valido para que el bloque 'init' no haga logout automático.
            val validUser = User(1, "Test", "User", "test@a.com", "USER", "date", "dir")
            coEvery { repository.getProfile() } returns validUser

            val viewModel = ProfileViewModel(application, repository, sessionManager)

            //limpiamos las verificaciones previas del init
            io.mockk.clearMocks(sessionManager, answers = false)

            Then("Debe limpiar la sesión y navegar al login") {
                runTest {
                    //aseguramos que el init haya terminado completamente
                    testDispatcher.scheduler.advanceUntilIdle()

                    viewModel.navigateToLogin.test {
                        //ejecutamos la acción manual
                        viewModel.logout()

                        testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe true
                    }
                }

                coVerify { sessionManager.clearLoginState() }
            }
        }
    }
})