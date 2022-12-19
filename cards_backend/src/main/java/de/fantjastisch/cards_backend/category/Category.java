package de.fantjastisch.cards_backend.category;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  String id; //todo
  String label;
//  UUID[] subCategories;
}
