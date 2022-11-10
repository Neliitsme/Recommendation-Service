package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "seenCategories")
class SeenCategories {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @Column(name = "seenCategories", unique = true)
    @ManyToOne
    @JoinColumn(name="recommendation")
    var recommendation: RecommendationsEntity? = null

    @Column(name = "categoryId", unique = true)
    var categoryID: UUID? = null

    var recommendationCoef: Int = 0

    var wasBought: Boolean = false
}