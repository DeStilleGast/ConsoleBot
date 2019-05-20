package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 13-5-2019
 */
class Ping : BaseApplication("ping") {

    override fun execute(context: Context) {
        val currentTime = System.currentTimeMillis()
        val pingMessage = context.reply("\uD83C\uDFD3 pinging...")
        pingMessage.thenAccept {
            val took = System.currentTimeMillis() - currentTime
            it.editMessage("\uD83C\uDFD3 is around ${took}MS while the api has ${context.getBot().ping}MS").queue()
        }
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.ROOT
    }

    override fun helpText(): String {
        return "Calculate the latency with discord and this bot"
    }
}