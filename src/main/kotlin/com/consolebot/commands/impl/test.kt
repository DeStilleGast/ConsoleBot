package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.processlist.ActiveApplications
import com.consolebot.processlist.TestProcess

/**
 * Created by DeStilleGast 14-5-2019
 */
class test : BaseApplication("test") {

    init {
    }

    override fun execute(context: Context) {
        ActiveApplications.keepRunning(TestProcess(context))
        context.reply("Application started")

        context.getDBUserAsync{ context.reply(it.terminalStyle) }
    }

}