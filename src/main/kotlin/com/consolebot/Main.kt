package com.consolebot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.EventListener

class Main(token: String) : EventListener {

    var bot: JDA = JDABuilder(AccountType.BOT)
        .setToken(token)
        .setAudioEnabled(false)
        .setAutoReconnect(true)
        .addEventListener(this)
        .build()

    override fun onEvent(event: Event?) {
        if(event is ReadyEvent){
            println("Bot is ready !")
            println("Bot is in ${event.guildTotalCount} guilds with ${event.guildAvailableCount} available")

            bot.shutdown()
        }
    }

}