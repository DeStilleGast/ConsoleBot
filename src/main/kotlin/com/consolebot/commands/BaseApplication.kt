package com.consolebot.commands

import com.consolebot.commands.exceptions.BotPermissionValidator
import com.consolebot.commands.exceptions.UserPermissionValidator
import com.consolebot.commands.exceptions.ValidationResult
import net.dv8tion.jda.core.Permission
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by DeStilleGast 12-5-2019
 */

/**
 * @param filename Set filename for command WITHOUT EXTENSION
 */
abstract class BaseApplication(var filename: String) {

    private val permissionRequirements: MutableList<ValidationResult> = ArrayList()

    init {
        permissionRequirements.add(BotPermissionValidator(this))
        permissionRequirements.add(UserPermissionValidator(this))
    }

    abstract fun execute(context: Context)

    /**
     * Return path for "application"
     */
    open fun getPath(): String {
        return KnownPaths.ROOT.path
    }

    /**
     * @return list of permissions that the bot requires to run this command
     */
    open fun requireBotPermission(): List<Permission> {
        return Collections.emptyList()
    }

    /**
     * @return list of permissions that the user requires to run this command
     */
    open fun requireUserPermission(): List<Permission> {
        return Collections.emptyList()
    }

    /**
     * @param context The context from the message (get user, channel and getGuild)
     * Use this function to apply extra validations, throw Exception when the command isn't allowed to run
     */
    fun extraValidationChecks(context: Context): List<String> {
        val errors: MutableList<String> = ArrayList()

        permissionRequirements.forEach {
            val result = it.validate(context)
            if (result.isNotEmpty()) {
                errors.add(result)
            }
        }

        return errors
    }

    protected fun registerValidation(validator: ValidationResult) {
        permissionRequirements.add(validator)
    }
}