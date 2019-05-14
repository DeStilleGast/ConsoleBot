package com.consolebot.commands.exceptions

import com.consolebot.commands.Context
import net.dv8tion.jda.core.entities.PrivateChannel
import net.dv8tion.jda.core.entities.TextChannel

/**
 * Created by DeStilleGast 14-5-2019
 */
class NSFWValidation : Validation() {
    override fun validate(context: Context): ValidationResult {
        return ValidationResult((context.channel is TextChannel && context.channel.isNSFW) || context.channel is PrivateChannel, "This application cannot run in this environment, please run this command again in a NSFW marked channel !")
    }
}