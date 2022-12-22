package ru.quipy.cartDemo.subscriber

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartBookedEvent
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.cartDemo.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.streams.AggregateSubscriptionsManager
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*
import javax.annotation.PostConstruct

@Service
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-booking")
class CartBookingSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val cartESService: EventSourcingService<UUID, CartAggregate, Cart>
) {
    private val logger: Logger = LoggerFactory.getLogger(CartBookingSubscriber::class.java)

    companion object {
        val TARGET_USER_ID: UUID = UUID.randomUUID();
    }

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartBookingSubscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        logger.info("Compare userId {} with target userId {}", event.userId, TARGET_USER_ID)
        if (event.cartId == TARGET_USER_ID) {
            logger.info("UserIds are same. Try to book cart")
            cartESService.update(event.cartId) { it.bookCart(event.userId) }
        } else {
            logger.info("UserIds are not same. Skip booking")
        }
    }

    @SubscribeEvent
    fun cartBookedSubscriber(event: CartBookedEvent) {
        if (event.cartId == TARGET_USER_ID) {
            logger.info("Cart {} was booked with target userId", event.cartId)
        }
    }
}