package com.consolebot.commands.impl.tools

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths
import net.dv8tion.jda.core.EmbedBuilder
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


/**
 * Created by DeStilleGast 23-5-2019
 */
class ColorApp : BaseApplication("color") {
    override fun execute(context: Context) {

        val colorArg = context.getText()
        var color: Color? = getColorFromFXColor(colorArg)

        if (color == null && colorArg.count { it == ' ' } == 2) {
            val split = colorArg.split(" ")

            try {
                color = Color(split[0].toFloat(), split[1].toFloat(), split[2].toFloat())
            } catch (ex: Exception) {
                try {
                    color = Color(split[0].toInt(), split[1].toInt(), split[2].toInt())
                } catch (ex: Exception) {
                }
            }
        }

        if (color == null) {
            context.reply("Could not replicate color")
            return
        }


        val tmp = BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR)
        for (x in 0 until tmp.width) {
            for (y in 0 until tmp.height) {
                tmp.setRGB(x, y, color.rgb)
            }
        }

        val embed = EmbedBuilder()
        embed.addField("RGB Color:", "[R: ${color.red}, G: ${color.green}, B: ${color.blue}]", false)
        embed.addField("ARGB Color:", "[A: ${color.alpha}, R: ${color.red}, G: ${color.green}, B: ${color.blue}]", false)
        embed.addField("HEX Color: (without alpha)", "#%02X%02X%02X".format(color.red, color.green, color.blue), false)
        embed.addField("HEX Color: (with alpha)", "#%02X%02X%02X%02X".format(color.alpha, color.red, color.green, color.blue), false)
        embed.setThumbnail("attachment://Color.png")
        embed.setColor(color)


        context.channel.sendMessage(embed.build())
            .addFile(toByteArrayAutoClosable(tmp, "png"), "Color.png").queue()

    }

    override fun getPath(): KnownPaths {
        return KnownPaths.TOOL
    }

    override fun helpText(): String {
        return listOf(
            "Shows a visual representation of a color and gives a RGB and hex color",
            "it currenly supports:",
            "- HEX (alpha colors at the end !)",
            "- RGB",
            "- names from color (english only)",
            "",
            "Examples:",
            "- red",
            "- dark blue",
            "- #aabbcc",
            "- 0x136362",
            "- 50 100 150",
            "- 0.4 0.7 0.1"
        ).joinToString("\n")

//        return "Shows visual colors from given input, supports name of the color, hex, rgb (also with space)" // needs better help page
    }

    private fun toByteArrayAutoClosable(image: BufferedImage, type: String): ByteArray {
        ByteArrayOutputStream().use { out ->
            ImageIO.write(image, type, out)
            return out.toByteArray()
        }
    }

    private fun getColorFromFXColor(input: String): Color? {
        try {
            val sceneColor = javafx.scene.paint.Color.valueOf(input.replace(" ", ""))
            return Color(
                sceneColor.red.toFloat(),
                sceneColor.green.toFloat(),
                sceneColor.blue.toFloat(),
                sceneColor.opacity.toFloat()
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}