package com.consolebot.commands.impl.user

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 20-5-2019
 */

class InServerCount : BaseApplication("servers"){
    override fun execute(context: Context) {
        val guildCount = context.getBot().asBot().shardManager.getUserById(context.user.id).mutualGuilds.size
        context.reply("I can see you in $guildCount server${if (guildCount != 1) "s" else ""}")
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.USER
    }


    override fun helpText(): String {
        return "get the server count of you that the bot can see"
    }
}