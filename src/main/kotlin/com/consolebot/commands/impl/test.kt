package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 14-5-2019
 */
class test : BaseApplication("test") {

    init {
    }

    override fun execute(context: Context) {
       
//        val test = System.currentTimeMillis()
//        Main.scheduledPool.schedule({context.reply("5 seconds have passed ${System.currentTimeMillis() - test}")}, 5, TimeUnit.SECONDS)

//        Main.scheduledPool.schedule({
//            ProcessManager.keepRunning(object : ActiveProcess() {
//                override fun onEvent(event: Event?) {
//                    if (event is MessageReceivedEvent && event.channel == context.channel) {
//                        if (event.author.isBot) return
//
//                        if (event.message.contentRaw.contains("quit")) {
//                            ProcessManager.closeApplication(this)
//                            context.reply("Application closed")
//                            return
//                        }
//                        context.reply(event.message.contentRaw)
//                    }
//                }
//            })
//            context.reply("App is running")
//        }, 5, TimeUnit.SECONDS)
//
//        context.reply("App is loading")

//        context.reply("Argument 0: ${context.arguments[0]}")
//        context.reply("Argument 1: ${context.arguments[1]}")
//        context.reply("Full line: ${context.getText()}")

//        ProcessManager.keepRunning(TestProcess(context))
//        context.reply("Application started")
//
//        context.getDBUserAsync{ context.reply(it.terminalStyle) }
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.ROOT
    }


    override fun helpText(): String {
        return "Yeah. the owner forgot to remove this command"
    }
}