package com.itmo.se.recommendationservice.recommendations.config

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.logic.Recommendation
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
    fun recommendationEsService(): EventSourcingService<UUID, RecommendationAggregate, Recommendation> =
        eventSourcingServiceFactory.create()
}