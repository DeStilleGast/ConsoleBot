package com.consolebot.settings

/**
 * Created by DeStilleGast 13-5-2019
 */

class Settings(
    var bottoken: String = "",
    var DBSchema: String = "",
    var DBHost: String = "",
    var DBPort: Int = 3306,
    var DBUsername: String = "",
    var DBPassword: String = "",
    var ShardTotal: Int = 1)