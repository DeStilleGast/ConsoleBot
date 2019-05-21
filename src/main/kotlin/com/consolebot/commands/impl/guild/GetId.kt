package com.consolebot.commands.impl.guild

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 21-5-2019
 */

class GetId : BaseApplication("id") {
    override fun execute(context: Context) {
        context.reply(context.getGuild()?.id ?: "Unknown")
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.GUILD
    }

    override fun helpText(): String {
        return "Prints the id of the guild"
    }

}