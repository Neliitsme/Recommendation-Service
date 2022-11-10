package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "seenItems")
class SeenItems {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @Column(name = "seenItems", unique = true)
    @ManyToOne
    @JoinColumn(name="recommendation")
    var recommendation: RecommendationsEntity? = null

    @Column(name = "itemId", unique = true)
    var itemID: UUID? = null

    var recommendationCoef: Int = 0

    var wasBought: Boolean = false
}