package com.consolebot.commands.exceptions

import com.consolebot.commands.Context
import net.dv8tion.jda.core.entities.PrivateChannel
import net.dv8tion.jda.core.entities.TextChannel

/**
 * Created by DeStilleGast 14-5-2019
 */
class NSFWValidation : ValidationResult() {
    override fun validate(context: Context): String {
        if(context.channel is PrivateChannel) return ""

        return if ((context.channel is TextChannel) && context.channel.isNSFW)
            ""
        else
            "This application cannot run in this environment, please run this command again in a NSFW marked channel !"
    }
}