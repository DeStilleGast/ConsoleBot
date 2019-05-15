package com.consolebot.processlist

import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by DeStilleGast 15-5-2019
 */
object ActiveApplications : EventListener{
    // prevent concurency exceptions
    private val runningApplications: MutableList<ActiveApplication> = CopyOnWriteArrayList()
    private var isRunning: Boolean = false

    fun keepRunning(app: ActiveApplication){
        runningApplications.add(app)
    }

    fun closeApplication(app: ActiveApplication){
        runningApplications.remove(app)
    }

    override fun onEvent(event: Event?) {
        if(isRunning) return

        isRunning = true
        runningApplications.forEach { it.onEvent(event) }
        isRunning = false
    }
}