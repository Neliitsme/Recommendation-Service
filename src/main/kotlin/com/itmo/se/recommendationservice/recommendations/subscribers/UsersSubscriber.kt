package com.itmo.se.recommendationservice.recommendations.subscribers

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.logic.Recommendation
import org.springframework.stereotype.Component
import ru.quipy.core.EventSourcingService
import ru.quipy.domain.Aggregate
import ru.quipy.domain.Event
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

// TODO: Update in correction to user service events (Check notion for this)
data class UsersAggregate(
    val userId: UUID,
) : Aggregate

data class UserCreatedEvent(
    val userId: UUID,
) : Event<UsersAggregate>(name = "USER_CREATED_EVENT")

@Component
class UsersSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val recommendationEsService: EventSourcingService<UUID, RecommendationAggregate, Recommendation>,
) {
    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UsersAggregate::class, "users::user-creation-processing-subscriber") {
            `when`(UserCreatedEvent::class) { event ->
                recommendationEsService.create { it.createNewRecommendation(userId = event.userId) }
            }
        }
    }
}