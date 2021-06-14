package com.sam.raytracer.desktop

import App
import com.badlogic.gdx.Application
import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object DesktopLauncher {

    private const val width = 2000
    private const val height = 1000

    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "Raytracer"
        config.width = width
        config.height = height
        LwjglApplication(App(width, height), config).logLevel = Application.LOG_DEBUG
    }
}