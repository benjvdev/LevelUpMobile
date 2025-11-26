package com.example.levelupmobile

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
object ProjectConfig : AbstractProjectConfig() {

    //aisla los tests para evitar estados compartidos
    override val isolationMode = IsolationMode.InstancePerLeaf

    private val testDispatcher = StandardTestDispatcher()

    //se ejecuta antes de todos los tests
    override suspend fun beforeProject() {
        super.beforeProject()
        Dispatchers.setMain(testDispatcher)
    }

    //se ejecuta al finalizar
    override suspend fun afterProject() {
        super.afterProject()
        Dispatchers.resetMain()
    }
}