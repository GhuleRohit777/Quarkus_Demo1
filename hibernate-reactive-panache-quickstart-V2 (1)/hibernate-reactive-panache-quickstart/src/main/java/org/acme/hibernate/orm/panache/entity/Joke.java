package org.acme.hibernate.orm.panache.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Joke extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false) // For MySQL
    public UUID id;

    @Column(nullable = false)
    public String question;

    @Column(nullable = false)
    public String answer;

    // Default constructor
    public Joke() {
    }

    public Joke(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
