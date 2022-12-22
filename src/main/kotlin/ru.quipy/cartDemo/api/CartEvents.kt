package ru.quipy.cartDemo.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CART_CREATED = "CART_CREATED_EVENT"
const val CART_BOOKED = "CART_BOOKED"

/**
 * Класс событие создания корзины продуктов
 */
@DomainEvent(name = CART_CREATED)
class CartCreatedEvent(
    val cartId: UUID,
    val userId: UUID
) : Event<CartAggregate>(
    name = CART_CREATED,
    createdAt = System.currentTimeMillis()
)

/**
 * Класс события бронирования корзины
 */
@DomainEvent(name = CART_BOOKED)
class CartBookedEvent(
    val cartId: UUID,
    val orderId: UUID,
    val sumPrice: Int
) : Event<CartAggregate>(
    name = CART_BOOKED,
    createdAt = System.currentTimeMillis()
)

@DomainEvent(name = "Extreme_big_event")
class ExtremeBigEventSetMutabilityFalse(
    val bigData: Array<Long>
) : Event<CartAggregate>(
    name = "Extreme_big_event"
)