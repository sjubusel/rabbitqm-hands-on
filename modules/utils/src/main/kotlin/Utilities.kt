package com.github.sjubusel.utils

import com.rabbitmq.client.ConnectionFactory

const val QUEUE_NAME = "hello-queue"
const val HOST = "rabbitmq"
const val USERNAME = "admin"
const val PASSWORD = "password"
const val PUB_SUB_EXCHANGE_NAME = "logs"
const val EMPTY = ""

fun initConnectionFactory(host: String = HOST): ConnectionFactory {
    val connectionFactory = ConnectionFactory()
    connectionFactory.host = host
    connectionFactory.username = USERNAME
    connectionFactory.password = PASSWORD
    return connectionFactory
}