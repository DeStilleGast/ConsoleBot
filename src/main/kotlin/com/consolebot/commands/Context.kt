package com.consolebot.commands

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

/**
 * Created by DeStilleGast 12-5-2019
 */
class Context(val channel: TextChannel, val user: User, val message: Message, val arguments: List<String>) {
    // TODO: change user to DBUser

    fun isUser(): Boolean{
        return !user.isBot || !user.isFake
    }

    fun getText(): String{
        return message.contentRaw
    }

    fun reply(msg: Any): Message{
        return channel.sendMessage(msg.toString()).complete()

        // TODO: add fallback (send DM)
    }

    fun getBot(): JDA {
        return channel.jda
    }

}