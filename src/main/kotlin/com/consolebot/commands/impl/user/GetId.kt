package com.consolebot.commands.impl.user

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 20-5-2019
 */
class GetId : BaseApplication("id") {
    override fun execute(context: Context) {
        context.reply(context.getPathUser()?.id ?: "No or multiple users found in path")
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.USER
    }


    override fun helpText(): String {
        return "get the id of this user"
    }
}