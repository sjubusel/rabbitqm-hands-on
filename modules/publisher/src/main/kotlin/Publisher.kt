package com.github.sjubusel.app

import com.github.sjubusel.utils.PUB_SUB_EXCHANGE_NAME
import com.github.sjubusel.utils.Severity
import com.github.sjubusel.utils.initConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.MessageProperties
import kotlin.random.Random

fun main() {
    initConnectionFactory().newConnection().use { connection -> // abstraction over sockets
        val channel = connection.createChannel()
        // fanout - ignore 'routingKey'
        // direct - exchange by 'routingKey'
        // topic - exchange by 'routingKey' using special cases for binding keys
        channel.exchangeDeclare(PUB_SUB_EXCHANGE_NAME, BuiltinExchangeType.TOPIC)

        val severity = "${generateRandomWord()}.${Severity.entries[Random.nextInt(Severity.entries.size)].name}"
        val message = "$severity: Hello, World!"
        channel.basicPublish(
            PUB_SUB_EXCHANGE_NAME,
            severity, // was QUEUE_NAME when no exchange is provided ("")
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.toByteArray()
        )
        println("[+] Published a '$message' to an exchange, which doesn't know about connected queues.")
    }

}

fun generateRandomWord(): String {
    val words = listOf(
        "adventure", "butterfly", "cascade", "dolphin", "elephant", "firefly", "guitar",
        "harmony", "island", "jungle", "kaleidoscope", "lighthouse", "mountain", "ocean",
        "phoenix", "quartz", "rainbow", "sunset", "telescope", "universe", "waterfall",
        "whisper", "xenial", "yearning", "zephyr"
    )
    return words[Random.nextInt(words.size)]
}
