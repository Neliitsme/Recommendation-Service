package com.itmo.se.recommendationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MvcRestMavenKotlinJpaApplication

fun main(args: Array<String>) {
    runApplication<MvcRestMavenKotlinJpaApplication>(*args)
}
