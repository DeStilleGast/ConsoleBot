package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.processlist.ActiveProcess
import com.consolebot.processlist.ProcessManager
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * Created by DeStilleGast 14-5-2019
 */
class test : BaseApplication("test") {

    init {
    }

    override fun execute(context: Context) {
//        val test = System.currentTimeMillis()
//        Main.scheduledPool.schedule({context.reply("5 seconds have passed ${System.currentTimeMillis() - test}")}, 5, TimeUnit.SECONDS)

        ProcessManager.keepRunning(object: ActiveProcess() {
            override fun onEvent(event: Event?) {
                if(event is MessageReceivedEvent && event.channel == context.channel){
                    if(event.author.isBot) return

                    if(event.message.contentRaw.contains("quit")){
                        ProcessManager.closeApplication(this)
                        context.reply("Application closed")
                        return
                    }
                    context.reply(event.message.contentRaw)
                }
            }
        })

        context.reply("App started")

//        context.reply("Argument 0: ${context.arguments[0]}")
//        context.reply("Argument 1: ${context.arguments[1]}")
//        context.reply("Full line: ${context.getText()}")

//        ProcessManager.keepRunning(TestProcess(context))
//        context.reply("Application started")
//
//        context.getDBUserAsync{ context.reply(it.terminalStyle) }
    }

}