package com.consolebot.commands.exceptions

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild

/**
 * Created by DeStilleGast 14-5-2019
 */
class BotPermissionValidator(val app: BaseApplication) : ValidationResult() {

    override fun validate(context: Context): String {
        val missingPermissions: MutableList<Permission> = ArrayList()

        app.requireBotPermission().forEach {
            val guild = context.getGuild()

            if (guild is Guild) {
                if (!guild.selfMember.hasPermission(it)) {
                    missingPermissions.add(it)
                }
            }
        }

        if(missingPermissions.isNotEmpty()) {
            return "This bot requires the following permissions to be able to launch this application:\n\t- ${missingPermissions.joinToString(
                "\n\t- "
            )}"
        }

        return ""
    }
}