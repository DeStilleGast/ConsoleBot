package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 13-5-2019
 */
class echo: BaseApplication("echo") {
    override fun execute(context: Context) {
        context.reply(context.arguments.joinToString(" "))
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.ROOT
    }

    override fun helpText(): String {
        return "returns the given text as a echo effect"
    }
}