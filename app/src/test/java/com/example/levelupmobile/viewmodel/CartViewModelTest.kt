package com.example.levelupmobile.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.example.levelupmobile.model.CartItem
import com.example.levelupmobile.repository.CartRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest : BehaviorSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    // mocks
    val application = mockk<Application>(relaxed = true)
    val repository = mockk<CartRepository>(relaxed = true)

    Given("Un CartViewModel") {

        // creamos un item de prueba
        val mockItem = CartItem(
            id = 1,
            name = "Producto Test",
            image = "url",
            price = 1000.0,
            quantity = 1
        )

        When("Se inicia el ViewModel") {
            // simulamos que el repositorio devuelve una lista con un item
            // es importante configurar esto antes de crear el viewmodel porque el init lo llama de inmediato
            every { repository.getCartItems() } returns flowOf(listOf(mockItem))

            val viewModel = CartViewModel(application, repository)

            Then("Debe cargar la lista de productos del carrito") {
                runTest {
                    viewModel.cartItems.test {
                        // el primer item suele ser la lista vacia inicial o la carga rapida
                        // esperamos a que llegue nuestra lista mockeada
                        val items = awaitItem()

                        // si turbine captura primero el emptyList del stateIn, esperamos el siguiente
                        if (items.isEmpty()) {
                            awaitItem() shouldBe listOf(mockItem)
                        } else {
                            items shouldBe listOf(mockItem)
                        }
                    }
                }
            }
        }

        When("Se elimina un item") {
            // configuramos el flujo base para que no falle el init
            every { repository.getCartItems() } returns flowOf(emptyList())
            val viewModel = CartViewModel(application, repository)

            viewModel.deleteItem(mockItem)

            Then("Debe llamar al repositorio para borrar el item") {
                runTest {
                    testDispatcher.scheduler.advanceUntilIdle()
                }
                coVerify { repository.deleteItem(mockItem) }
            }
        }

        When("Se cambia la cantidad a un valor positivo") {
            every { repository.getCartItems() } returns flowOf(emptyList())
            val viewModel = CartViewModel(application, repository)

            val newQuantity = 5
            viewModel.onQuantityChanged(mockItem, newQuantity)

            Then("Debe llamar al repositorio para actualizar la cantidad") {
                runTest {
                    testDispatcher.scheduler.advanceUntilIdle()
                }
                coVerify { repository.updateQuantity(mockItem, newQuantity) }
            }
        }

        When("Se cambia la cantidad a cero") {
            every { repository.getCartItems() } returns flowOf(emptyList())
            val viewModel = CartViewModel(application, repository)

            val newQuantity = 0
            viewModel.onQuantityChanged(mockItem, newQuantity)

            Then("Debe llamar al repositorio para borrar el item en vez de actualizarlo") {
                runTest {
                    testDispatcher.scheduler.advanceUntilIdle()
                }
                // verificamos que se llame a delete y no a update
                coVerify { repository.deleteItem(mockItem) }
                coVerify(exactly = 0) { repository.updateQuantity(any(), any()) }
            }
        }
    }
})