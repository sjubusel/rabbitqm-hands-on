package com.github.sjubusel.app

import com.github.sjubusel.utils.QUEUE_NAME
import com.github.sjubusel.utils.initConnectionFactory

fun main() {
    initConnectionFactory().newConnection().use { connection -> // abstraction over sockets
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null) // idempotent

        val message = "Hello, World!"
        channel.basicPublish("", QUEUE_NAME, null, message.toByteArray())
        println("[+] Published a '$message'")
    }

}