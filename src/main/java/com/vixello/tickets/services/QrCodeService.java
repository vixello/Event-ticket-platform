package com.vixello.tickets.services;

import com.vixello.tickets.domain.entities.QrCode;
import com.vixello.tickets.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
