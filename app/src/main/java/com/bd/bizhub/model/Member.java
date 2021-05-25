package com.bd.bizhub.model;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public final class Member {
    @NotNull
    private final String id;
    @NotNull
    private final String name;

    // Standard getters & setters
    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }


    public Member(@NotNull Document document) {
        this.id = (String) Objects.requireNonNull(document.get("_id"));
        this.name = (String) Objects.requireNonNull(document.get("name"));

    }
}
