package ar.com.flow.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.io.IOException;
import java.util.Optional;

import static ar.com.flow.chat.Event.Type.USER_LEFT;

public class ChatSocketHandler implements WebSocketHandler {

    private UnicastProcessor<Event> eventPublisher;
    private Flux<String> outputEvents;
    private ObjectMapper mapper;

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    public ChatSocketHandler(UnicastProcessor<Event> eventPublisher, Flux<Event> events) {
        this.eventPublisher = eventPublisher;
        this.mapper = new ObjectMapper();
        this.outputEvents = Flux.from(events).map(this::toJSON);
    }

    @Override
    public Mono<Void> handle(WebSocketSession userSession) {
        log.info("Initiated WebSocketSession: {}", userSession.getId());

        WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(eventPublisher);

        return userSession.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toEvent)
                .doOnNext(subscriber::onNext)
                .doOnError(subscriber::onError)
                .doOnComplete(subscriber::onComplete)
                .zipWith(userSession.send(outputEvents.map(userSession::textMessage)))
                .then();
    }


    private Event toEvent(String json) {
        try {
            return mapper.readValue(json, Event.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    private String toJSON(Event event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class WebSocketMessageSubscriber {
        private UnicastProcessor<Event> eventPublisher;
        private Optional<Event> lastReceivedEvent = Optional.empty();

        public WebSocketMessageSubscriber(UnicastProcessor<Event> eventPublisher) {
            this.eventPublisher = eventPublisher;
        }

        public void onNext(Event event) {
            lastReceivedEvent = Optional.of(event);
            eventPublisher.onNext(event);
        }

        public void onError(Throwable error) {
            //TODO log error
            error.printStackTrace();
        }

        public void onComplete() {

            lastReceivedEvent.ifPresent(event -> eventPublisher.onNext(
                    Event.type(USER_LEFT)
                            .withPayload()
                            .user(event.getUser())
                            .build()));
        }

    }
}
