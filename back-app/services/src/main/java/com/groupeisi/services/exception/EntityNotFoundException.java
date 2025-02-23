package com.groupeisi.services.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityNotFoundException extends RuntimeException {
     String message;
     public EntityNotFoundException(String message) {
          super(message);
          this.message = message;
     }
}
