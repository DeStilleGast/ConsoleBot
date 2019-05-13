package com.consolebot.settings

import com.beust.klaxon.Klaxon
import java.io.File

/**
 * Created by DeStilleGast 13-5-2019
 */
class SettingsLoader {

    fun getSettings(): Settings {
        val settingsFile = File("config.json") // Settings file

        // Check if file exists, if not create fresh new config
        if (!settingsFile.exists()) {
            val defaultJson = Klaxon().toJsonString(Settings("", "", "", "", "", ""))
            val writer = settingsFile.printWriter()
            writer.use { it.print(defaultJson) }
            writer.close()
        }

        // read config
        val reader = settingsFile.inputStream()
        val readString = reader.bufferedReader().use { it.readText() }

        return Klaxon().parse<Settings>(readString)!!
    }
}