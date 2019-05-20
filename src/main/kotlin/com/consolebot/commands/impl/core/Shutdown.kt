package com.consolebot.commands.impl.core

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths
import com.consolebot.commands.validations.OwnerValidator

/**
 * Created by DeStilleGast 14-5-2019
 */
class Shutdown : BaseApplication("shutdown") {

    init {
        registerValidation(OwnerValidator())
    }

    override fun execute(context: Context) {
        context.getBot().shutdown()
     }

    /**
     * Return path for "application"
     */
    override fun getPath(): KnownPaths{
        return KnownPaths.SYSTEM
    }
}