package com.consolebot

import com.consolebot.settings.SettingsLoader

fun main() {
    val settings = SettingsLoader().getSettings()

    if (settings.bottoken.isEmpty()) {
        println("NO TOKEN FOUND/LOADED")
        println("Please edit 'config.json' to add the bottoken :D")
        return
    }

    Main(settings)

    // -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
}