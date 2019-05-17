package com.consolebot.commands.impl.core

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 17-5-2019
 */
class Info : BaseApplication("info") {
    override fun execute(context: Context) {
        context.reply("Information about this bot:\n" +
                "Uptime: N/A\n" +
                "Serving in ${context.getBot().guilds.size} guilds\n" +
                "Shard id: ${context.getBot().shardInfo.shardId}")
    }

    override fun getPath(): String {
        return KnownPaths.SYSTEM.path
    }
}