package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context

/**
 * Created by DeStilleGast 13-5-2019
 */
class Ping : BaseApplication("ping") {

    override fun execute(context: Context) {
        val currentTime = System.currentTimeMillis()
        val pingMessage = context.reply("\uD83C\uDFD3 pinging...")
        val took = System.currentTimeMillis() - currentTime
        pingMessage.editMessage("\uD83C\uDFD3 is around ${took}MS while the api has ${context.getBot().ping}MS").complete()
    }
}