package com.vixello.tickets.domain.dtos;

import com.vixello.tickets.domain.CreateTicketTypeRequest;
import com.vixello.tickets.domain.entities.EventStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestDto {
    @NotBlank(message = "Event name is required")
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;

    @NotBlank(message = "Venue details is required")
    private String venue;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;

    @NotNull(message = "Event status must be provided")
    private EventStatusEnum status;

    @NotEmpty(message = "At least one ticket type is required")
    private List<CreateTicketTypeRequestDto> ticketTypes = new ArrayList<>();
}
