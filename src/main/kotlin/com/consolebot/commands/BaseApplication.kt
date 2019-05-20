package com.consolebot.commands

import com.consolebot.commands.validations.BotPermissionValidator
import com.consolebot.commands.validations.UserPermissionValidator
import com.consolebot.commands.validations.Validation
import com.consolebot.commands.validations.ValidationResult
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

    private val permissionRequirements: MutableList<Validation> = ArrayList()

    init {
        permissionRequirements.add(BotPermissionValidator(this))
        permissionRequirements.add(UserPermissionValidator(this))
    }

    abstract fun execute(context: Context)

    /**
     * Return path for "application"
     */
    abstract fun getPath(): KnownPaths

    /**
     * Return the help text the application
     */
    abstract fun helpText(): String


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
    fun runValidationCheck(context: Context): List<ValidationResult> {
        val errors: MutableList<ValidationResult> = ArrayList()

        permissionRequirements.forEach {
            val result = it.validate(context)
            if (!result.hasPassed) {
                errors.add(result)
            }
        }

        return errors
    }

    protected fun registerValidation(validator: Validation) {
        permissionRequirements.add(validator)
    }
}