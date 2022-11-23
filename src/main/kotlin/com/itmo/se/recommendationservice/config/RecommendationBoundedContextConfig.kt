package com.itmo.se.recommendationservice.config

import com.itmo.se.recommendationservice.api.RecommendationAggregate
import com.itmo.se.recommendationservice.logic.Recommendation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import java.util.*


@Configuration
class RecommendationBoundedContextConfig {

    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun accountEsService(): EventSourcingService<UUID, RecommendationAggregate, Recommendation> =
        eventSourcingServiceFactory.create()
}