package com.itmo.se.recommendationservice.trends.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.*

@Component
class ItemOrderCountsView(
    private val itemOrderCountsRepository: ItemOrderCountsRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(ItemOrderCountsView::class.java)

    @PostConstruct
    fun init() {
    }
}

@Table
data class ItemOrderCounts(
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @Column(unique = true)
    var itemId: UUID? = null,

    @Column
    var orderedTimes: Int = 0
)

interface ItemOrderCountsRepository : JpaRepository<ItemOrderCounts, UUID>