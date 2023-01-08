package de.fantjastisch.cards_backend.link;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Builder
@Data
public class Link {
    String name;
    UUID source;
    UUID target;
}
