package com.consolebot.commands.impl.tools

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.KnownPaths
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


/**
 * Created by DeStilleGast 23-5-2019
 */
class ColorApp : BaseApplication("color") {
    override fun execute(context: Context) {
        val tmp = BufferedImage(50, 25, BufferedImage.TYPE_4BYTE_ABGR)

        var color: Color? = null
        var colorArg = context.arguments[0]

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
                color = Color.decode(context.arguments[0])
            } catch (ex: Exception) {
                color = hackColorByName(context.arguments[0])
            }
        }

        if (color == null) {
            context.reply("Could not replicate color")
            return
        }

        for (x in 0 until tmp.width) {
            for (y in 0 until tmp.height) {
                tmp.setRGB(x, y, color.rgb)

            }
        }

        context.channel.sendFile(toByteArrayAutoClosable(tmp, "png"), "Color.png").queue()

    }

    override fun getPath(): KnownPaths {
        return KnownPaths.TOOL
    }

    override fun helpText(): String {
        return "Shows visual colors from given input"
    }

    private fun toByteArrayAutoClosable(image: BufferedImage, type: String): ByteArray {
        ByteArrayOutputStream().use { out ->
            ImageIO.write(image, type, out)
            return out.toByteArray()
        }
    }

    private fun hackColorByName(colorName: String): Color? {
        return try {
            Color::class.java.getField(colorName.toUpperCase()).get(null) as Color
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