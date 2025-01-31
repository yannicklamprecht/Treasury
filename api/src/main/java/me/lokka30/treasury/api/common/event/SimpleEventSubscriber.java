/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a simple event subscriber. A simple event subscriber shall be used if you don't
 * want to block the event execution.
 *
 * <p>Examples:
 * <pre>
 * EventBus eventBus = EventBus.INSTANCE;
 * eventBus.subscribe(
 *   eventBus.subscriptionFor(AnEvent.class)
 *     .withPriority(EventPriority.HIGH)
 *     .whenCalled(event -> {
 *       // do stuff
 *     });
 *     .completeSubscription()
 * );
 *
 * public class MyEventListener extends SimpleEventSubscriber&#60;MyEvent&#62; {
 *
 *   public MyEventListener() {
 *     super(MyEvent.class, EventPriority.NORMAL);
 *   }
 *
 *   &#64;Override
 *   public void subscribe(MyEvent event) {
 *     // do stuff
 *   }
 * }
 *
 * // then you register
 * EventBus.INSTANCE.subscribe(new MyEventListener());
 * </pre>
 *
 * @param <T> event type
 * @author MrIvanPlays
 * @see EventSubscriber
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#V1_1 v1.1}
 */
public abstract class SimpleEventSubscriber<T> extends EventSubscriber<T> {

    public SimpleEventSubscriber(@NotNull Class<T> eventClass) {
        super(eventClass);
    }

    public SimpleEventSubscriber(@NotNull Class<T> eventClass, boolean ignoreCancelled) {
        super(eventClass, ignoreCancelled);
    }

    public SimpleEventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority
    ) {
        super(eventClass, priority);
    }

    public SimpleEventSubscriber(
            @NotNull Class<T> eventClass, @NotNull EventPriority priority, boolean ignoreCancelled
    ) {
        super(eventClass, priority, ignoreCancelled);
    }

    /**
     * Treasury's {@link EventBus} calls this method whenever a {@link EventBus#fire(Object)}
     * occurs with the event this subscription listens for. Difference between
     * {@link EventSubscriber#onEvent(Object)} and this is that {@code onEvent} has the ability
     * to block event execution whilst this method cannot. Reason why we're in
     * <b>Simple</b>EventExecutor
     *
     * @param event the event
     */
    public abstract void subscribe(@NotNull T event);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Completion onEvent(@NotNull T event) {
        try {
            subscribe(event);
            return Completion.completed();
        } catch (Throwable error) {
            return Completion.completedExceptionally(error);
        }
    }

    @Override
    public String toString() {
        return "Simple" + super.toString();
    }

}
