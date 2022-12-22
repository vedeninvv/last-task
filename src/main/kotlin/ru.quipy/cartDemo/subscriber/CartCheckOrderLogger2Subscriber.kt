package ru.quipy.cartDemo.subscriber

import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartBookedEvent
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import javax.annotation.PostConstruct

@Component
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-check-order-logger2")
class CartCheckOrderLogger2Subscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(CartCheckOrderLogger2Subscriber::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartLogger2Subscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        logger.info("Cart created {}. Logger #2", event.cartId)
        Thread.sleep(2000)  //hard computing...
    }

    @SubscribeEvent
    fun cartBookedSubscriber(event: CartBookedEvent) {
        logger.info("Cart booked {} with sumPrice", event.cartId, event.sumPrice)
    }
}