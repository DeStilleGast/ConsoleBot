package com.consolebot.commands.impl.guild.other

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 21-5-2019
 */
class name : BaseApplication("name") {
    override fun execute(context: Context) {
        context.reply(context.getPathGuild()?.name ?: "Unknown")
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.GUILDOTHER
    }

    override fun helpText(): String {
        return "Returns the name of the guild"
    }

}