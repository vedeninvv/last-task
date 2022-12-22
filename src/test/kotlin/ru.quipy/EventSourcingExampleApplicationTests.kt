package ru.quipy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.logic.Cart
import ru.quipy.cartDemo.subscriber.CartBookingSubscriber
import ru.quipy.core.EventSourcingService
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EventSourcingExampleApplicationTests {
    @Autowired
    private lateinit var cartESService: EventSourcingService<UUID, CartAggregate, Cart>

    @Test
    fun createCart() {
        val cartId = UUID.randomUUID();
        val userId = UUID.randomUUID();
        println("___TEST_INFO: TARGET_USER_ID $CartBookingSubscriber.TARGET_USER_ID")
        println("___TEST_INFO: cartId $cartId")
        println("___TEST_INFO: userId $userId")

        cartESService.create { it.createNewCart(cartId, userId) };

        val state = cartESService.getState(cartId);
        Assertions.assertNotNull(state);
        Assertions.assertEquals(cartId, state!!.getId());
    }

    @Test
    fun createAndBookCart() {
        val sumPrice = 1234;
        val cartId = UUID.randomUUID();
        val userId = UUID.randomUUID();

        cartESService.create { it.createNewCart(cartId, userId) };

        val state = cartESService.getState(cartId);
        if (state != null) {
            Assertions.assertEquals(cartId, state.getId());
            Assertions.assertTrue(state.getMutability()!!)
            Assertions.assertNull(state.getOrder());
        } else {
            fail("State is null after creating");
        }

        cartESService.update(cartId) { it.bookCart(userId) };

        val bookedState = cartESService.getState(cartId);
        if (bookedState != null) {
            Assertions.assertEquals(cartId, bookedState.getId());
            Assertions.assertFalse(bookedState.getMutability()!!);
            Assertions.assertEquals(sumPrice, bookedState.getOrder()!!.sumPrice)
        } else {
            fail("State is null after booking")
        }
    }

    @Test
    fun updateWithNotExistingId() {
        val cartId = UUID.randomUUID();
        val userId = UUID.randomUUID();

        assertThrows<IllegalArgumentException> {
            cartESService.update(cartId) { it.createNewCart(cartId, userId) };
        }
    }

    @Test
    fun createWithSameId() {
        val cartId = UUID.randomUUID();
        val userId = UUID.randomUUID();

        cartESService.create { it.createNewCart(cartId, userId) };
        assertThrows<RuntimeException> {
            cartESService.create { it.createNewCart(cartId, userId) };
        }
    }

    @Test
    fun updateToExistId() {
        val cart1Id = UUID.randomUUID();
        val user1Id = UUID.randomUUID();
        val cart2Id = UUID.randomUUID();
        val user2Id = UUID.randomUUID();

        cartESService.create { it.createNewCart(cart1Id, user1Id) };
        cartESService.create { it.createNewCart(cart2Id, user2Id) };

        //обновляем id первой корзины на id второй, но userId не меняем
        assertThrows<RuntimeException> {
            cartESService.update(cart1Id) { it.createNewCart(cart2Id, user1Id) }
        }

        //смотрим какой вернет userID
        val state = cartESService.getState(cart2Id)
        Assertions.assertEquals(user1Id, state!!.getUserId())
        Assertions.assertEquals(user2Id, state.getUserId())
    }
}
