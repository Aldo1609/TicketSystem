package com.aldob.dtos.responses;

import lombok.Data;

@Data
public class TicketStatisticsResponse {

    private Long totalTickets;
    private Long openTickets;
    private Long inProgressTickets;
    private Long resolvedTickets;
    private Long closedTickets;
    private Long criticalTickets;

    public TicketStatisticsResponse(
            Long totalTickets,
            Long openTickets,
            Long inProgressTickets,
            Long resolvedTickets,
            Long closedTickets,
            Long criticalTickets
    ) {
        this.totalTickets = totalTickets;
        this.openTickets = openTickets;
        this.inProgressTickets = inProgressTickets;
        this.resolvedTickets = resolvedTickets;
        this.closedTickets = closedTickets;
        this.criticalTickets = criticalTickets;
    }

}
