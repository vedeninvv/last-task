package ru.quipy.cartDemo.subscriber

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartBookedEvent
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import javax.annotation.PostConstruct

@Service
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-logger")
class CartLoggerSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(CartLoggerSubscriber::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartLoggerSubscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        logger.info("Cart created {}", event.cartId)
    }

    @SubscribeEvent
    fun cartBookedSubscriber(event: CartBookedEvent) {
        logger.info("Cart booked {} with sumPrice", event.cartId, event.sumPrice)
    }
}