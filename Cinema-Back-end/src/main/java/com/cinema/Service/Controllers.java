package com.cinema.Service;

import com.cinema.Repository.TicketRepository;
import com.cinema.entities.Ticket;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class Controllers  {
    @Autowired
    private TicketRepository ticketRepository ;
    @PostMapping(value = "/payerTickets")
    public List<Ticket> payerTickets(@RequestBody DataForm dataform){
        List<Ticket> tickets = new ArrayList<Ticket>() ;
        dataform.getTickets().forEach(id-> {
            Ticket t =  ticketRepository.findById(id).get() ;
            t.setNomClient(dataform.getNomClient()) ;
            t.setReserve(true);
            t.setCodePayement(dataform.getCodePayement());
            ticketRepository.save(t);
            tickets.add(t);
        });

        System.out.println(tickets.size());
        return tickets ;
    }

    @Data
    static class DataForm{
        private String nomClient ;
        private int codePayement ;
        private List<Long> tickets = new ArrayList<Long>() ;
    }
}
