package com.itmo.se.recommendationservice.recommendations.projections

import com.itmo.se.recommendationservice.catalog.api.CatalogAggregate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

const val ADD_ITEM_TO_CATALOG_EVENT = "ADD_ITEM_TO_CATALOG_EVENT"
const val REMOVE_ITEM_FROM_CATALOG_EVENT = "REMOVE_ITEM_FROM_CATALOG_EVENT"

@Component
class ItemExistenceInCatalogView(
    private val itemsInCatalogRepository: ItemsInCatalogRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(ItemExistenceInCatalogView::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(CatalogAggregate::class, "item-added-to-calalog") {
            `when`(AddItemToCatalogEvent::class) { event ->
                val fromDb: Optional<ItemsInCatalog> = itemsInCatalogRepository.findItemById(event.itemId)

                fromDb.ifPresentOrElse(
                    {},
                    {
                        val toSave = ItemsInCatalog(itemId = event.itemId, categoryId = event.categoryID)
                        itemsInCatalogRepository.save(toSave)
                    }
                )
                logger.info(
                    "The item with id=${event.itemId} and category_id=${event.categoryID} " +
                            "was added to catalog"
                )
            }
        }

        subscriptionsManager.createSubscriber(CatalogAggregate::class, "item-removed-from-catalog") {
            `when`(RemoveItemFromCatalogEvent::class) { event ->
                val fromDb = itemsInCatalogRepository.findItemById(event.itemId)
                val toDelete = fromDb.get()
                itemsInCatalogRepository.delete(toDelete)
                logger.info(
                    "The item with id=${event.itemId} was removed to catalog"
                )
            }
        }
    }

}

@Document
data class ItemsInCatalog(
    @Id
    var id: UUID? = null,

    val itemId: UUID? = null,

    val categoryId: UUID? = null
)

@Repository
interface ItemsInCatalogRepository : MongoRepository<ItemsInCatalog, UUID> {
    fun findItemById(itemId: UUID): Optional<ItemsInCatalog>
}


@DomainEvent(name = ADD_ITEM_TO_CATALOG_EVENT)
data class AddItemToCatalogEvent(
    val itemId: UUID,
    val categoryID: UUID
) : Event<CatalogAggregate>(name = ADD_ITEM_TO_CATALOG_EVENT)

@DomainEvent(name = REMOVE_ITEM_FROM_CATALOG_EVENT)
data class RemoveItemFromCatalogEvent(
    val itemId: UUID
) : Event<CatalogAggregate>(name = REMOVE_ITEM_FROM_CATALOG_EVENT)
