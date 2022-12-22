package ru.quipy.cartDemo.subscriber

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.cartDemo.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.streams.AggregateSubscriptionsManager
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*
import javax.annotation.PostConstruct

@Component
@AggregateSubscriber(aggregateClass = CartAggregate::class, subscriberName = "cart-cycle-subscriber")
class CartCycleSubscriber(
    private val subscriptionsManager: AggregateSubscriptionsManager,
    private val cartESService: EventSourcingService<UUID, CartAggregate, Cart>
) {
    private val logger: Logger = LoggerFactory.getLogger(CartCycleSubscriber::class.java)

    companion object {
        val cycledUUID: UUID = UUID.randomUUID();
    }

    @PostConstruct
    fun init() {
        subscriptionsManager.subscribe<CartAggregate>(this)
        logger.info("Init CartCycleSubscriber")
    }

    @SubscribeEvent
    fun cartCreatedSubscriber(event: CartCreatedEvent) {
        if (event.cartId.equals(cycledUUID)) {
            logger.info("Cart created {}", event.cartId)
            cartESService.create { it.createNewCart(cycledUUID, UUID.randomUUID()) }
        }
    }
}