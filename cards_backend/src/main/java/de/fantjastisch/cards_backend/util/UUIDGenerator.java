package de.fantjastisch.cards_backend.util;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor
public class UUIDGenerator {
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
