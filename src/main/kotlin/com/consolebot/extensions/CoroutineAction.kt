package com.consolebot.extensions

import kotlinx.coroutines.future.await
import net.dv8tion.jda.core.requests.RestAction

suspend fun<V> RestAction<V>.await(): V = submit().await()