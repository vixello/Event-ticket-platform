package com.vixello.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {

    private String name;

    private String description;

    private Double price;

    private Integer totalAvailable;
}
