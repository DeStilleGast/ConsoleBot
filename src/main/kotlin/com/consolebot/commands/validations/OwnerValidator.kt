package com.consolebot.commands.validations

import com.consolebot.GlobalVariables
import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 14-5-2019
 */
class OwnerValidator : Validation {
    override fun validate(context: Context): ValidationResult {
        return ValidationResult(context.user.idLong in GlobalVariables.ConsoleOwners, "Access denied (code: 5)")
    }
}