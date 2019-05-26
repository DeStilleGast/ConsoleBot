package com.consolebot.commands.impl.user.other

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths
import com.consolebot.commands.validations.OrUserPermissionValidator
import com.consolebot.commands.validations.OrValidator
import com.consolebot.commands.validations.RoleValidator
import net.dv8tion.jda.core.Permission

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

//        guild.controller.kick(guild.getMember(user), "Kicked by ${context.user.name}, Reason: " + (if (context.getText().isEmpty()) "No reason given" else context.getText()))
        context.reply("Kicked by ${context.user.name}, Reason: " + (if (context.getText().isEmpty()) "No reason given" else context.getText()))


//        if(PermissionUtil.canInteract(context.user,)
//        context.reply("W.I.P. user kicked")
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