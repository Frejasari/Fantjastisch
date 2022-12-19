package de.fantjastisch.cards_backend;

import de.fantjastisch.cards_backend.CreatedResponse.CreatedResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreatedResponse extends ResponseEntity<CreatedResponseData> {

  public CreatedResponse(final String id) {
    super(new CreatedResponseData(id), HttpStatus.CREATED);
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreatedResponseData {

    String id;
  }
}
