package ru.quipy.cartDemo.subscriber

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
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-check-order-logger3")
class CartCheckOrderLogger3Subscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager
) {
    private val logger: Logger = LoggerFactory.getLogger(CartCheckOrderLogger3Subscriber::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartLogger3Subscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        logger.info("Cart created {}. Logger #3", event.cartId)
        Thread.sleep(4000)  //very hard computing...
    }

    @SubscribeEvent
    fun cartBookedSubscriber(event: CartBookedEvent) {
        logger.info("Cart booked {} with sumPrice", event.cartId, event.sumPrice)
    }
}