package com.example.levelupmobile.viewmodel

import android.app.Application
import com.example.levelupmobile.remote.Product
import com.example.levelupmobile.repository.CartRepository
import com.example.levelupmobile.repository.ProductRepository
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
class HomeViewModelTest : BehaviorSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    //mocks relajados para evitar errores si no definimos algo
    val application = mockk<Application>(relaxed = true)
    val productRepository = mockk<ProductRepository>(relaxed = true)
    val cartRepository = mockk<CartRepository>(relaxed = true)

    Given("Un HomeViewModel") {

        //datos de prueba
        val mockProduct = Product(
            id = 1,
            name = "PS5",
            description = "Consola",
            price = 500.0,
            image = "img",
            category = "consolas",
            disponible = true
        )

        When("Se inicia el ViewModel") {
            //configuramos el mock antes de crear el viewmodel
            coEvery { productRepository.getProducts() } returns listOf(mockProduct)

            val viewModel = HomeViewModel(application, productRepository, cartRepository)

            Then("Debe cargar la lista de productos automáticamente") {
                runTest(testDispatcher) {
                    //avanzamos el tiempo hasta que terminen las tareas pendientes
                    testDispatcher.scheduler.advanceUntilIdle()

                    //verificamos directamente el valor del estado
                    viewModel.products.value shouldBe listOf(mockProduct)
                }
            }
        }

        When("Se añade un producto al carrito") {
            //en este caso usamos un mock vacio para la carga inicial
            coEvery { productRepository.getProducts() } returns emptyList()

            val viewModel = HomeViewModel(application, productRepository, cartRepository)

            Then("Debe llamar al repositorio y mostrar la notificación por un tiempo") {
                runTest(testDispatcher) {
                    //estado inicial deberia ser falso
                    viewModel.showAddedToCartOverlay.value shouldBe false

                    //ejecutamos la accion de añadir
                    viewModel.onAddToCartClicked(mockProduct)

                    //avanzamos un poco para que cambie el estado
                    testDispatcher.scheduler.advanceTimeBy(100)

                    //verificamos que la notificacion se muestra
                    viewModel.showAddedToCartOverlay.value shouldBe true

                    //avanzamos el tiempo casi hasta el final del delay
                    testDispatcher.scheduler.advanceTimeBy(1000)

                    //todavia deberia estar mostrandose
                    viewModel.showAddedToCartOverlay.value shouldBe true

                    //avanzamos lo suficiente para pasar el tiempo total
                    testDispatcher.scheduler.advanceTimeBy(200)

                    //finalmente la notificacion debe ocultarse
                    viewModel.showAddedToCartOverlay.value shouldBe false
                }

                //verificamos que se llamo al repositorio
                coVerify { cartRepository.addProductToCart(mockProduct) }
            }
        }
    }
})