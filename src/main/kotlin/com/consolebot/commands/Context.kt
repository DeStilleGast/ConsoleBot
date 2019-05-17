package com.consolebot.commands

import com.consolebot.Main
import com.consolebot.database.DBUser
import com.consolebot.database.DatabaseUserWrapper
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.*
import java.util.concurrent.CompletionStage

/**
 * Created by DeStilleGast 12-5-2019
 */
class Context(val channel: MessageChannel, val user: User, val message: Message, val arguments: List<String>) {

    fun isUser(): Boolean {
        return !user.isBot || !user.isFake
    }

    fun getText(): String {
        return message.contentRaw
    }

    fun reply(msg: Any): CompletionStage<Message> {
        if (channel is TextChannel) {
            return if (channel.guild.selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)) {
                channel.sendMessage(msg.toString()).submit()
            } else {
                user.openPrivateChannel()
                    .submit()
                    .thenCompose { dmChannel ->
                        dmChannel.sendMessage("$msg\n\nYou got this message in your DM because I'm unable to react in the channel where you executed the command")
                            .submit()
                            .exceptionally {
                                Main.LOGGER.error("FAILED SENDING DM MESSAGE \"${it.message}\" (user blocked bot) {'${message.contentRaw}'}")
                                null
                            }
                    }
            }
        }

        // so its not a textchannel so it is a DM (can't be a groupchannel, bots are banned from there)
        return channel.sendMessage(msg.toString()).submit()
    }


    fun getBot(): JDA {
        return channel.jda
    }

    fun isPM(): Boolean {
        return channel is PrivateChannel
    }

    fun getGuild(): Guild? {
        if (channel is TextChannel) {
            return channel.guild
        }
        return null
    }

    fun getDBUser(): DBUser {
        return DatabaseUserWrapper.getUser(user).get()
    }

    fun getDBUserAsync(callback: (u: DBUser) -> Any) {
        DatabaseUserWrapper.getUser(user).thenAccept { callback(it) }
    }
}