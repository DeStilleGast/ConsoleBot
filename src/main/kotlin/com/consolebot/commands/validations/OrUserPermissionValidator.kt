package com.consolebot.commands.validations

import com.consolebot.commands.Context
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild

/**
 * Created by DeStilleGast 23-5-2019
 */
class OrUserPermissionValidator(private val permissionList: List<Permission>) : Validation {

    override fun validate(context: Context): ValidationResult {
        val missingPermissions: MutableList<Permission> = ArrayList()

        permissionList.forEach {
            val guild = context.getGuild()

            if (guild is Guild) {
                if (!guild.getMember(context.user).hasPermission(it)) {
                    missingPermissions.add(it)
                }
            }
        }

        return ValidationResult(missingPermissions.isEmpty(), "You are required the following permissions to be able to launch this application:\n\t- ${missingPermissions.joinToString(
            "\n\t- "
        )}")
    }
}