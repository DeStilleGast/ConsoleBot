package com.consolebot.commands.validations

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild

/**
 * Created by DeStilleGast 14-5-2019
 */
class BotPermissionValidator(private val app: BaseApplication) : Validation {

    override fun validate(context: Context): ValidationResult {
        val missingPermissions: MutableList<Permission> = ArrayList()

        app.requireBotPermission().forEach {
            val guild = context.getGuild()

            if (guild is Guild) {
                if (!guild.selfMember.hasPermission(it)) {
                    missingPermissions.add(it)
                }
            }
        }

        return ValidationResult(missingPermissions.isEmpty(), "This bot requires the following permissions to be able to launch this application:\n\t- ${missingPermissions.joinToString(
            "\n\t- "
        )}")
    }
}