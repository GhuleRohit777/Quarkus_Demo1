package org.acme.hibernate.orm.panache.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.hibernate.orm.panache.entity.Joke;

@ApplicationScoped
public class JokeRepository implements PanacheRepository<Joke> {
    // You can add custom methods here if needed
}
