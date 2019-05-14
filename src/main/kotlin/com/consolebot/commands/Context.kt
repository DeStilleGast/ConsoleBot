package com.consolebot.commands

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*

/**
 * Created by DeStilleGast 12-5-2019
 */
class Context(val channel: MessageChannel, val user: User, val message: Message, val arguments: List<String>) {
    // TODO: change user to DBUser

    fun isUser(): Boolean {
        return !user.isBot || !user.isFake
    }

    fun getText(): String {
        return message.contentRaw
    }

    fun reply(msg: Any): Message {
        return channel.sendMessage(msg.toString()).complete()

        // TODO: add fallback (send DM)
    }

    fun getBot(): JDA {
        return channel.jda
    }

    fun isPM(): Boolean{
        return channel is PrivateChannel
    }

    fun getGuild(): Guild? {
        if(channel is TextChannel){
            return channel.guild
        }
        return null
    }
}