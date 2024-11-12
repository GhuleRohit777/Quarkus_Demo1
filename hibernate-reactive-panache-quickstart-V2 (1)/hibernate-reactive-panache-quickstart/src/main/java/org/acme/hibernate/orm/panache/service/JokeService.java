package org.acme.hibernate.orm.panache.service;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.hibernate.orm.panache.entity.Joke;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class JokeService {

    private final WebClient webClient;

    @Inject
    public JokeService(Vertx vertx) {
        // Initialize WebClient using Vert.x
        this.webClient = WebClient.create(vertx);
    }

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<List<Joke>> fetchAndStoreJokes(int count) {
        List<Uni<Joke>> jokeRequests = IntStream.range(0, count)
                .mapToObj(i -> webClient.getAbs("https://official-joke-api.appspot.com/random_joke")
                        .send()
                        .onItem().transform(response -> {
                            String setup = response.bodyAsJsonObject().getString("setup");
                            String punchline = response.bodyAsJsonObject().getString("punchline");
                            return new Joke(setup, punchline);
                        }))
                .collect(Collectors.toList());

        return Uni.combine().all().unis(jokeRequests)
                .combinedWith(list -> (List<Joke>) list)
                .onItem().transformToUni(jokes -> sessionFactory.withTransaction(session -> {
                    List<Uni<Void>> persistOperations = jokes.stream()
                            .map(joke -> session.persist(joke))
                            .collect(Collectors.toList());
                    return Uni.combine().all().unis(persistOperations).combinedWith(ignored -> jokes);
                }));
    }
}
