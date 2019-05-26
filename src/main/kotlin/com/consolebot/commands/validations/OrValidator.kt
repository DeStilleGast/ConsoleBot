package com.consolebot.commands.validations

import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 23-5-2019
 */
class OrValidator(private val val1: Validation, private val val2: Validation) : Validation {

    override fun validate(context: Context): ValidationResult {
        val validationResultA = val1.validate(context)
        val validationResultB = val2.validate(context)

        if(validationResultA.hasPassed || validationResultB.hasPassed){
            return ValidationResult(true, "Both validations have passed")
        }

        return ValidationResult(false, "You need to solve 1 of these 2 issues:\n\t> " +
                validationResultA.notPassedReason +
                "\nOR\n\t> " +
                validationResultB.notPassedReason)
    }
}