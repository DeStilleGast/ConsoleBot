package com.consolebot.commands.validations

import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 23-5-2019
 */
class RoleValidator(private val roleName: String) : Validation {

    override fun validate(context: Context): ValidationResult {
        val guild = context.getGuild() ?: return ValidationResult(true, "Only guilds have roles")

        return ValidationResult(
            guild.getMember(context.user).roles.map { it.name.toLowerCase() }.contains(roleName.toLowerCase()),
            "You are missing the '$roleName' role to use this application")
    }
}