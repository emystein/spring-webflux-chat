package ar.com.flow.chat;

import ar.com.flow.chat.Event.Type;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static ar.com.flow.chat.Event.Type.*;
import static java.util.Arrays.asList;

public class UserStats {
    Map<String, Stats> userStatsMap = new ConcurrentHashMap<>();

    public UserStats(Flux<Event> events, Sinks.Many<Event> eventPublisher) {
        events
                .filter(type(CHAT_MESSAGE, USER_JOINED))
                .subscribe(this::onChatMessage);
        events
                .filter(type(USER_LEFT))
                .map(Event::getUser)
                .map(User::getAlias)
                .subscribe(userStatsMap::remove);
        events
                .filter(type(USER_JOINED))
                .map((event) -> Event.type(USER_STATS)
                        .withPayload()
                        .systemUser()
                        .property("stats", new HashMap<>(userStatsMap))
                        .build())
                .subscribe((userStatsEvent) -> eventPublisher.emitNext(userStatsEvent, Sinks.EmitFailureHandler.FAIL_FAST));
    }

    private static Predicate<Event> type(Type... types){
        return event ->  asList(types).contains(event.getType());
    }

    private void onChatMessage(Event event) {
        String alias = event.getUser().getAlias();
        Stats stats = userStatsMap.computeIfAbsent(alias, s -> new Stats(event.getUser()));
        stats.onChatMessage(event);
    }

    private static class Stats {
        private User user;
        private long lastMessage;
        private AtomicInteger messageCount = new AtomicInteger();

        public Stats(User user) {
            this.user = user;
        }

        public void onChatMessage(Event event) {
            lastMessage = event.getTimestamp();
            if(CHAT_MESSAGE == event.getType()) messageCount.incrementAndGet();
        }

        public User getUser() {
            return user;
        }

        public long getLastMessage() {
            return lastMessage;
        }

        public int getMessageCount() {
            return messageCount.get();
        }
    }
}
