package com.github.sjubusel.app

import com.github.sjubusel.utils.QUEUE_NAME
import com.github.sjubusel.utils.initConnectionFactory
import com.rabbitmq.client.DeliverCallback
import java.time.LocalDateTime

fun main() {
    initConnectionFactory().newConnection()?.let { connection ->
        val channel = connection.createChannel()
        val durable = true
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null)
        val prefetchCount = 1
        channel.basicQos(prefetchCount)

        val deliveryCallback = DeliverCallback { _, delivery ->
            val message = String(delivery.body)
            println("[x] Received '$message' at ${LocalDateTime.now()}")
            try {
                Thread.sleep(5000L)
            } finally {
                channel.basicAck(delivery.envelope.deliveryTag, false)
                println("[x] Processing is Done")
            }
        }
        val autoAck = false
        channel.basicConsume(QUEUE_NAME, autoAck, deliveryCallback, {})
    }
}