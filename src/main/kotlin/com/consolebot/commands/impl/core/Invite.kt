package com.consolebot.commands.impl.core

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 17-5-2019
 */
class Invite : BaseApplication("invite") {
    override fun execute(context: Context) {
        context.reply("To let me join one of your servers, you will need to click on this link: ${context.getBot().asBot().getInviteUrl()}")
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.SYSTEM
    }
}