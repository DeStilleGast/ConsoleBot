package com.consolebot.processlist

import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.EventListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by DeStilleGast 15-5-2019
 */
object ProcessManager : EventListener{
    // prevent concurency exceptions
    private val runningApplications: MutableList<ActiveProcess> = CopyOnWriteArrayList()
    private var isRunning: Boolean = false

    fun keepRunning(process: ActiveProcess){
        runningApplications.add(process)
    }

    fun closeApplication(process: ActiveProcess){
        runningApplications.remove(process)
    }

    override fun onEvent(event: Event?) {
        if(isRunning) return // just to be sure

        isRunning = true
        runningApplications.forEach { it.onEvent(event) }
        isRunning = false
    }
}