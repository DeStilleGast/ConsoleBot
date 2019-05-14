package com.consolebot.commands.exceptions

import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 14-5-2019
 */
abstract class Validation {
    abstract fun validate(context: Context): ValidationResult
}