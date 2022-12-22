package ru.quipy.cartDemo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import java.util.*

@Configuration
class CartDemoConfig {

    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun cartESService(): EventSourcingService<UUID, CartAggregate, Cart> =
        eventSourcingServiceFactory.create()

}