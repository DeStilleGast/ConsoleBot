package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 14-5-2019
 */
class test : BaseApplication("test") {

    init {
    }

    override fun execute(context: Context) {
        context.reply("Argument 0: ${context.arguments[0]}")
        context.reply("Argument 1: ${context.arguments[1]}")
        context.reply("Full line: ${context.getText()}")

//        ActiveApplications.keepRunning(TestProcess(context))
//        context.reply("Application started")
//
//        context.getDBUserAsync{ context.reply(it.terminalStyle) }
    }

}