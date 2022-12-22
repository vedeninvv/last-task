package ru.quipy.cartDemo.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

/**
 * Класс агрегата корзины продуктов
 */
@AggregateType(aggregateEventsTableName = "carts")
class CartAggregate : Aggregate