package com.consolebot.commands.impl.user.other

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths

/**
 * Created by DeStilleGast 20-5-2019
 */

class InServerCount : BaseApplication("servers"){
    override fun execute(context: Context) {
        val user = context.getPathUser(0)
        if(user == null){
            context.reply("No or multiple users found in path")
        }else{
            val guildCount = context.getBot().asBot().shardManager.getUserById(user.id).mutualGuilds.size
            context.reply("I can see this user in $guildCount server${if (guildCount != 1) "s" else ""}")
        }
    }

    override fun getPath(): KnownPaths {
        return KnownPaths.USEROTHER
    }


    override fun helpText(): String {
        return "get the server count of this user that the bot can see"
    }
}