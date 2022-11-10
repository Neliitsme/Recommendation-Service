package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Table(name = "seenCategories")
class SeenCategories {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @Column(name = "recomendationId", unique = true)
    var recomendationId: UUID? = null

    @Column(name = "categoryId", unique = true)
    var categoryID: UUID? = null

    var recommendationCoef: Int = 0

    var wasBought: Boolean = false
}