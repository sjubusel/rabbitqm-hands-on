package com.github.sjubusel.app

import com.github.sjubusel.utils.HOST
import com.github.sjubusel.utils.QUEUE_NAME
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback

fun main() {
    val factory = ConnectionFactory()
    factory.host = HOST
    factory.newConnection().use { connection ->
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println(" [*] Waiting for messages. To exit press CTRL+C")

        val deliveryCallback = DeliverCallback { _, delivery ->
            val message = String(delivery.body)
            println(" [x] Received '$message'")
        }
        channel.basicConsume(QUEUE_NAME, true, deliveryCallback, {})
    }
}