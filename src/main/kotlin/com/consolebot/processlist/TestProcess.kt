package com.consolebot.processlist

import com.consolebot.commands.Context
import net.dv8tion.jda.core.events.Event

/**
 * Created by DeStilleGast 15-5-2019
 */
class TestProcess(private val context: Context) : ActiveApplication(){

    init{

    }

    val current = System.currentTimeMillis()

    override fun onEvent(event: Event?) {
        if(System.currentTimeMillis() - current > 5000){
            context.reply(System.currentTimeMillis() - current)
            ActiveApplications.closeApplication(this)
        }
    }
}