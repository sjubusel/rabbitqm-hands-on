package com.github.sjubusel.app

import com.github.sjubusel.utils.HOST
import com.github.sjubusel.utils.QUEUE_NAME
import com.rabbitmq.client.ConnectionFactory

fun main() {
    val connectionFactory = ConnectionFactory()
    connectionFactory.host = HOST
    connectionFactory.newConnection().use { connection -> // abstraction over sockets
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null) // idempotent

        val message = "Hello, World!"
        channel.basicPublish("", QUEUE_NAME, null, message.toByteArray())
    }

}