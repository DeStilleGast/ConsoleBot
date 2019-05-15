package com.consolebot.processlist

import net.dv8tion.jda.core.events.Event

/**
 * Created by DeStilleGast 15-5-2019
 */
abstract class ActiveApplication {
    abstract fun onEvent(event: Event?)
}