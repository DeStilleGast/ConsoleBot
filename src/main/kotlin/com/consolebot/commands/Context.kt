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
class Context(
    val channel: MessageChannel,
    val user: User,
    val message: Message,
    val arguments: List<String>,
    val pathArguments: List<Pair<String, Any?>>
) {

    fun isUser(): Boolean {
        return !user.isBot || !user.isFake
    }

    fun getText(): String {
        return message.contentRaw
    }

    fun reply(msg: Any?): CompletionStage<Message> {
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

    fun getPathUser(index: Int = 0): User? {
        val userContext = pathArguments.filter { it.first == "<userid>" }[index].second
        if (userContext is ArrayList<*>) {
            if (userContext.size == 1) {
                return (userContext[0] as User)
            }
        } else if (userContext is User) {
            return userContext
        }
        return null
    }

    fun getPathGuild(index: Int = 0): Guild? {
        val guildContext = pathArguments.filter { it.first == "<guildid>" }[index].second
        if (guildContext is List<*>) {
            if (guildContext.size == 1) {
                return (guildContext[0] as Guild)
            }
        } else if (guildContext is Guild) {
            return guildContext
        }
        return null
    }
}