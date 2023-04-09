package ar.com.flow.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@SpringBootApplication
public class ReactiveChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveChatApplication.class, args);
    }

    @Bean
    public Sinks.Many<Event> eventPublisher() {
        return Sinks.many().replay().limit(25);
    }

    @Bean
    public Flux<Event> events(Sinks.Many<Event> eventPublisher) {
        return eventPublisher.asFlux();
    }

    @Bean
    public RouterFunction<ServerResponse> routes(){
        return RouterFunctions.route(
                GET("/"),
                request -> ServerResponse.ok().body(BodyInserters.fromResource(new ClassPathResource("public/index.html")))
        );
    }

    @Bean
    public HandlerMapping webSocketMapping(Sinks.Many<Event> eventPublisher, Flux<Event> events) {
        Map<String, Object> map = new HashMap<>();
        map.put("/websocket/chat", new ChatSocketHandler(eventPublisher, events));
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(map);

        //Without the order things break :-/
        simpleUrlHandlerMapping.setOrder(10);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public UserStats userStats(Flux<Event> events, Sinks.Many<Event> eventPublisher){
        return new UserStats(events, eventPublisher);
    }
}
