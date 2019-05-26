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

        var color: Color? = null
        var colorArg = context.getText()

        if (colorArg.startsWith("0x")) colorArg = colorArg.replace("0x", "#")

        if (colorArg.startsWith("#")) {
            if (colorArg.length == "#123456".length) {
                color = hex2Rgb(colorArg)
            } else if (colorArg.length == "#12345678".length) {
                color = hex2Rgba(colorArg)
            }
        }

        if (color == null) {
            try {
                color = hackColorByName(colorArg)
            } catch (ex: Exception) {
                color = Color.decode(colorArg)
            }
        }

        if(color == null && colorArg.count { it == ' ' } == 2){
            val split = colorArg.split(" ")

            try{
                color = Color(split[0].toFloat(), split[1].toFloat(), split[2].toFloat())
            }catch (ex: Exception){
                try{
                    color = Color(split[0].toInt(), split[1].toInt(), split[2].toInt())
                }catch (ex: Exception){}
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


        context.channel.sendMessage(embed.build()).addFile(toByteArrayAutoClosable(tmp, "png"), "Color.png").queue()
//        context.channel.sendFile(toByteArrayAutoClosable(tmp, "png"), "Color.png").queue()

    }

    override fun getPath(): KnownPaths {
        return KnownPaths.TOOL
    }

    override fun helpText(): String {
        return "Shows visual colors from given input, supports name of the color, hex, rgb (also with space)" // needs better help page
    }

    private fun toByteArrayAutoClosable(image: BufferedImage, type: String): ByteArray {
        ByteArrayOutputStream().use { out ->
            ImageIO.write(image, type, out)
            return out.toByteArray()
        }
    }

    private fun hackColorByName(colorName: String): Color? {
        return try {
            Color::class.java.getField(colorName.replace(" ", "_").toUpperCase()).get(null) as Color
        } catch (e: IllegalArgumentException) {
            null
        } catch (e: IllegalAccessException) {
            null
        } catch (e: NoSuchFieldException) {
            null
        } catch (e: SecurityException) {
            null
        }
    }

    /**
     *
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    private fun hex2Rgb(colorStr: String): Color {
        return Color(
            Integer.valueOf(colorStr.substring(1, 3), 16),
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16)
        )
    }

    /**
     *
     * @param colorStr e.g. "#FFFFFFFF"
     * @return
     */
    private fun hex2Rgba(colorStr: String): Color {
        return Color(
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16),
            Integer.valueOf(colorStr.substring(7, 9), 16),
            Integer.valueOf(colorStr.substring(1, 3), 16)
        )
    }
}