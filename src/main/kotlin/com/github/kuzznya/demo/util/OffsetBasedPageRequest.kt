package com.github.kuzznya.demo.util

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class OffsetBasedPageRequest(offset: Int, limit: Int, sort: Sort = Sort.unsorted()) : Pageable {
    private val limit: Int
    private val offset: Int
    private val sort: Sort

    init {
        require(offset >= 0) { "Offset index must not be less than zero!" }
        require(limit >= 1) { "Limit must not be less than one!" }
        this.limit = limit
        this.offset = offset
        this.sort = sort
    }

    override fun getPageNumber(): Int {
        return offset / limit
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getOffset(): Long {
        return offset.toLong()
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun next(): Pageable {
        return OffsetBasedPageRequest(offset + limit, limit, sort)
    }

    fun previous(): OffsetBasedPageRequest {
        return if (hasPrevious()) OffsetBasedPageRequest(offset - limit, limit, sort) else this
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) previous() else first()
    }

    override fun first(): Pageable {
        return OffsetBasedPageRequest(0, limit, sort)
    }

    override fun withPage(pageNumber: Int): Pageable {
        return OffsetBasedPageRequest(limit * pageNumber, limit, sort)
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }
}