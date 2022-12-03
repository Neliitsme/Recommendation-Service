package com.itmo.se.recommendationservice.trends.projections

import com.itmo.se.recommendationservice.orders.api.OrderAggregate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.*

const val ADD_ITEM_TO_ORDER_EVENT = "ADD_ITEM_TO_ORDER"
const val REMOVE_ITEM_FROM_ORDER_EVENT = "REMOVE_ITEM_FROM_ORDER"

@Component
class ItemOrderCountsView(
    private val itemOrderCountsRepository: ItemOrderCountsRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(ItemOrderCountsView::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(OrderAggregate::class, "item-order-counts-increase") {
            `when`(AddItemToOrderEvent::class) { event ->
                val fromDb: Optional<ItemOrderCounts> = itemOrderCountsRepository.findByItemId(event.itemId)

                fromDb.ifPresentOrElse(
                    { entity ->
                        entity.orderedTimes += event.countItem
                        itemOrderCountsRepository.save(entity)
                    },
                    {
                        val toSave = ItemOrderCounts(itemId = event.itemId, orderedTimes = event.countItem)
                        itemOrderCountsRepository.save(toSave)
                    }
                )
                logger.info("The item with id=${event.itemId}, count=${event.countItem} was added to the order")
            }
        }

        subscriptionsManager.createSubscriber(OrderAggregate::class, "item-order-counts-decrease") {
            `when`(RemoveItemFromOrderEvent::class) { event ->
                val fromDb = itemOrderCountsRepository.findByItemId(event.itemId)
                // Optional всегда isPresent, т.к. чтобы убрать товар(ы) из
                // корзины нужно для начале его(их) туда добавить
                // Думаю, что ситуаций, когда toSave.orderedTimes - event.countItem < 0
                // по упомянутой выше причине
                val toSave = fromDb.get()
                toSave.orderedTimes -= event.countItem

                itemOrderCountsRepository.save(toSave)
                logger.info("The item with id=${event.itemId}, count=${event.countItem} was removed from the order")
            }
        }
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
    var orderedTimes: Long = 0
)

@Repository
interface ItemOrderCountsRepository : JpaRepository<ItemOrderCounts, UUID> {
    fun findByItemId(itemId: UUID): Optional<ItemOrderCounts>
}


@DomainEvent(name = ADD_ITEM_TO_ORDER_EVENT)
data class AddItemToOrderEvent(
    val itemId: UUID,
    val countItem: Long,
    val orderId: UUID,
    val userId: UUID
) : Event<OrderAggregate>(name = ADD_ITEM_TO_ORDER_EVENT)

@DomainEvent(name = REMOVE_ITEM_FROM_ORDER_EVENT)
data class RemoveItemFromOrderEvent(
    val itemId: UUID,
    val countItem: Long,
    val orderId: UUID
) : Event<OrderAggregate>(name = REMOVE_ITEM_FROM_ORDER_EVENT)
