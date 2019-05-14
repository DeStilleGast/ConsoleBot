package com.consolebot.commands.impl.core

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 14-5-2019
 */
class Shutdown : BaseApplication("shutdown") {
    override fun execute(context: Context) {
        context.getBot().shutdown()
    }

    /**
     * Return path for "application"
     */
    override fun getPath(): String{
        return KnownPaths.SYSTEM.path
    }


}