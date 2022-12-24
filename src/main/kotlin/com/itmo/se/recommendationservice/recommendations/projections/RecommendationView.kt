package com.itmo.se.recommendationservice.recommendations.view

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.api.RecommendationCreatedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class RecommendationView(
    private val recommendationRepository: RecommendationRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager,
) {
    private val logger: Logger = LoggerFactory.getLogger(RecommendationView::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(RecommendationAggregate::class, "recommendation-creating") {
            `when`(RecommendationCreatedEvent::class) { event ->

                val recommendation: RecommendationDto? = recommendationRepository
                    .findRecommendationDtoByRecommendationId(event.itemId)
                if (recommendation != null) {
                    val toSave = RecommendationDto(recommendationId = event.recommendationId, userId = event.userId)
                    recommendationRepository.save(toSave)
                }

                logger.info(
                    "The recommendation with id=${event.recommendationId} and userId=${event.userId} " +
                            "was added to cache"
                )
            }
        }
    }
}

@Repository
interface RecommendationRepository : MongoRepository<RecommendationDto, UUID> {
    fun findRecommendationDtoByRecommendationId(recommendationId: UUID): RecommendationDto?
}

data class RecommendationDto(
    val recommendationId: UUID,
    val userId: UUID,
)