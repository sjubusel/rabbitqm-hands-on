package com.github.sjubusel.app

import com.github.sjubusel.utils.EMPTY
import com.github.sjubusel.utils.PUB_SUB_EXCHANGE_NAME
import com.github.sjubusel.utils.initConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.MessageProperties

fun main() {
    initConnectionFactory().newConnection().use { connection -> // abstraction over sockets
        val channel = connection.createChannel()
        channel.exchangeDeclare(PUB_SUB_EXCHANGE_NAME, BuiltinExchangeType.FANOUT)

        val message = "Hello, World!"
        channel.basicPublish(
            PUB_SUB_EXCHANGE_NAME,
            EMPTY, // was QUEUE_NAME
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.toByteArray()
        )
        println("[+] Published a '$message' to an exchange, which doesn't know about connected queues.")
    }

}