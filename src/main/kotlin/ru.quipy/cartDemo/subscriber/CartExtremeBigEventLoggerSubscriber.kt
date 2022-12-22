package ru.quipy.cartDemo.subscriber

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.cartDemo.api.ExtremeBigEventSetMutabilityFalse
import ru.quipy.streams.AggregateSubscriptionsManager
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import javax.annotation.PostConstruct

@Component
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-extreme-big-logger")
class CartExtremeBigEventLoggerSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(CartCheckOrderLogger3Subscriber::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartExtremeBigEventLoggerSubscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        logger.info("Cart created {}. Logger CartExtremeBigEventLoggerSubscriber", event.cartId)
    }

    @SubscribeEvent
    fun cartBookedSubscriber(event: ExtremeBigEventSetMutabilityFalse) {
        logger.info("EXTREME_BIG_EVENT")
    }
}