package com.consolebot.commands.validations

import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 14-5-2019
 */
interface Validation {
    fun validate(context: Context): ValidationResult
}