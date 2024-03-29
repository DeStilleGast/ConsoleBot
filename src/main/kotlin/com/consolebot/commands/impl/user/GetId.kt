package com.consolebot.commands.impl.user

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 21-5-2019
 */
class GetId : BaseApplication("id") {
    override fun execute(context: Context) {
        context.reply(context.user.id)
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.USER
    }


    override fun helpText(): String {
        return "get the id from your account"
    }
}