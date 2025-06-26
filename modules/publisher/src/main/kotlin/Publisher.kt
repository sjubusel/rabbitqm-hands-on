package com.github.sjubusel.app

import com.github.sjubusel.utils.QUEUE_NAME
import com.github.sjubusel.utils.initConnectionFactory
import com.rabbitmq.client.MessageProperties

fun main() {
    initConnectionFactory().newConnection().use { connection -> // abstraction over sockets
        val channel = connection.createChannel()
        val durable = true
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null) // idempotent

        val message = "Hello, World!"
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toByteArray())
        println("[+] Published a '$message'")
    }

}