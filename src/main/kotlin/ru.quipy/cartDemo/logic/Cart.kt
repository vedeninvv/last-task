package ru.quipy.cartDemo.logic

import ru.quipy.cartDemo.api.CartAggregate
import ru.quipy.cartDemo.api.CartBookedEvent
import ru.quipy.cartDemo.api.CartCreatedEvent
import ru.quipy.cartDemo.api.ExtremeBigEventSetMutabilityFalse
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

/**
 * Класс состояния агрегата корзины продуктов
 */
class Cart : AggregateState<UUID, CartAggregate> {
    private lateinit var cartId: UUID;
    private lateinit var userId: UUID;
    private var mutability: Boolean? = null;
    private var order: Order? = null;

    override fun getId() = cartId;
    fun getUserId() = userId;
    fun getMutability() = mutability;
    fun getOrder() = order;

    /**
     * Команда создания корзины продуктов
     */
    fun createNewCart(cartId: UUID = UUID.randomUUID(), userId: UUID): CartCreatedEvent {
        return CartCreatedEvent(cartId, userId);
    }

    /**
     * Команда бронирования корзины продуктов
     */
    fun bookCart(userId: UUID): CartBookedEvent {
        if (!userId.equals(this.userId)) {
            throw IllegalArgumentException("This user can not book");
        }
        /*
        По идее здесь нужно рассчитать стоимость всех продуктов, но для простоты, так как в агрегате сейчас нет даже
        этих продуктов, будем присваивать просто любое значение
         */
        val sumPrice = 1234;

        return CartBookedEvent(cartId, userId, sumPrice);
    }

    /**
     * Transition функция для ивента создания корзины
     */
    @StateTransitionFunc
    fun createNewCart(event: CartCreatedEvent) {
        mutability = true;
        cartId = event.cartId;
        userId = event.userId;
    }

    /**
     * Transition функция для ивента бронирования корзины
     */
    @StateTransitionFunc
    fun bookCart(event: CartBookedEvent) {
        mutability = false;
        order = Order(event.orderId, event.sumPrice);
    }

    @StateTransitionFunc
    fun extremeBigEvent(event: ExtremeBigEventSetMutabilityFalse) {
        mutability = false;
    }
}

/**
 * Сущность заказа, который создается при бронировании
 */
data class Order(
    val orderId: UUID,
    val sumPrice: Int
) {}