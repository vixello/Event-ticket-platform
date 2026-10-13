package com.vixello.tickets.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {
    @NotBlank(message = " Ticket type name is required")
    private String name;

    @NotBlank(message = " Description is required")
    @PositiveOrZero(message = "Price must be zero or greater")
    private String description;

    private Double price;

    private Integer totalAvailable;
}
