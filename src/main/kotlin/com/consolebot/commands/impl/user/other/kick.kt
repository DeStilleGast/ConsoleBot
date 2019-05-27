package com.consolebot.commands.impl.user.other

import com.consolebot.Main
import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths
import com.consolebot.commands.validations.OrUserPermissionValidator
import com.consolebot.commands.validations.OrValidator
import com.consolebot.commands.validations.RoleValidator
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.utils.PermissionUtil

/**
 * Created by DeStilleGast 23-5-2019
 */
class kick : BaseApplication("kick") {

    init {
        registerValidation(
            OrValidator(RoleValidator("can kick"),
            OrUserPermissionValidator(listOf(Permission.KICK_MEMBERS)))
        )
    }

    override fun execute(context: Context) {
        val guild = context.getGuild()

        if(guild == null){
            context.reply("This application only works in guilds")
            return
        }

        val user = context.getPathUser()
        if(user == null){
            context.reply("Unknown user")
            return
        }

        val member = guild.getMember(user)

        if(PermissionUtil.canInteract(guild.selfMember, member)){
            context.reply("Missing permission: Cannot kick this user!\n - This user has the same or a higher role than this bot")
            return
        }

        val kickReason = if (context.getText().isEmpty()) "No reason given" else context.getText()

        guild.controller.kick(member, "Kicked by ${context.user.name}, Reason: $kickReason").submit().thenAccept{
//            context.reply("${user.name} was kicked by ${context.user.name}, Reason: $kickReason")
            context.reply("User kicked")
        }.exceptionally {
            context.reply("Could not kick this user for the following reason: \n ${it.localizedMessage}")
            Main.LOGGER.error("Could not kick ${user.name} from guild ${guild.name} -> ${it.printStackTrace()}")
            context.logException("Could not kick user (${user.name}) in guild (${guild.name})", it)
            null
        }
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.USEROTHER
    }

    override fun helpText(): String {
        return "Kicks the user from guild"
    }

    override fun requireBotPermission(): List<Permission> {
        return listOf(Permission.KICK_MEMBERS)
    }
}