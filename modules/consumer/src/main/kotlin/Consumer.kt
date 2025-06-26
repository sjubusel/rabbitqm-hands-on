package com.github.sjubusel.app

import com.github.sjubusel.utils.PUB_SUB_EXCHANGE_NAME
import com.github.sjubusel.utils.QUEUE_NAME
import com.github.sjubusel.utils.Severity
import com.github.sjubusel.utils.initConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.DeliverCallback
import java.time.LocalDateTime

fun main(args: Array<String>) {
    initConnectionFactory().newConnection()?.let { connection ->
        val channel = connection.createChannel()
        channel.exchangeDeclare(PUB_SUB_EXCHANGE_NAME, BuiltinExchangeType.DIRECT)
        val durable = true
        val queueName = QUEUE_NAME + args.size
        channel.queueDeclare(queueName, durable, false, false, null) // idempotent
        (args.takeIf { it.isNotEmpty() } ?: arrayOf(Severity.ERROR.name))
            .forEach { severity ->
                println(severity)
                channel.queueBind(queueName, PUB_SUB_EXCHANGE_NAME, severity)
            }

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
        channel.basicConsume(queueName, autoAck, deliveryCallback, {})
    }
}