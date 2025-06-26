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
        channel.exchangeDeclare(PUB_SUB_EXCHANGE_NAME, BuiltinExchangeType.DIRECT)

        val severity = Severity.entries[Random.nextInt(Severity.entries.size)]
        val message = "$severity: Hello, World!"
        channel.basicPublish(
            PUB_SUB_EXCHANGE_NAME,
            severity.name, // was QUEUE_NAME
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.toByteArray()
        )
        println("[+] Published a '$message' to an exchange, which doesn't know about connected queues.")
    }

}