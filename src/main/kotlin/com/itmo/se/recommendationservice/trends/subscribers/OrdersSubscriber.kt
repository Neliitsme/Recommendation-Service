package com.itmo.se.recommendationservice.trends.subscribers

import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.logic.Trend
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.quipy.core.EventSourcingService
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class OrdersSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val trendEsService: EventSourcingService<UUID, TrendAggregate, Trend>
) {
    private val logger: Logger = LoggerFactory.getLogger(OrdersSubscriber::class.java)

    @PostConstruct
    fun init() {
    }
}