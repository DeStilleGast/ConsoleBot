package com.consolebot

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.CommandManager
import com.consolebot.settings.Settings
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.EventListener
import org.reflections8.Reflections

class Main(botSettings: Settings) : EventListener {

    private val commandManager: CommandManager = CommandManager()
    private val bot: JDA

    init {
        findAndRegisterCommands()
        bot = JDABuilder(AccountType.BOT)
            .setToken(botSettings.bottoken)
            .setAudioEnabled(false)
            .setAutoReconnect(true)
            .addEventListener(this)
            .addEventListener(commandManager)
            .build()
    }

    override fun onEvent(event: Event?) {
        if (event is ReadyEvent) {
            println("Bot is ready !")
            println("Bot is in ${event.guildTotalCount} guilds with ${event.guildAvailableCount} available")
        }
    }

    /**
     * Attempt and find commands in commands implementations (and subpackages)
     */
    fun findAndRegisterCommands() {
        val reflections = Reflections("com.consolebot.commands.impl")

        val subTypes = reflections.getSubTypesOf(BaseApplication::class.java)

        subTypes.forEach {
            commandManager.registerCommand(it.newInstance())
        }
    }

}