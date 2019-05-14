package com.consolebot.commands.exceptions

import com.consolebot.GlobalVariables
import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 14-5-2019
 */
class OwnerValidator : ValidationResult() {
    override fun validate(context: Context): String {
        return if(context.user.idLong !in GlobalVariables.ConsoleOwners) "Access denied (code: 5)" else ""
    }
}