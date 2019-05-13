package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 13-5-2019
 */
class echo: BaseApplication("echo") {
    override fun execute(context: Context) {
        context.reply(context.arguments.joinToString(" "))
    }
}