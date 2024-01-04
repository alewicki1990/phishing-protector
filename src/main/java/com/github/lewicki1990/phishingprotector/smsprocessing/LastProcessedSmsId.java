package com.github.lewicki1990.phishingprotector.smsprocessing;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="last_processed_sms_id")
@ToString
public class LastProcessedSmsId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    public LastProcessedSmsId(){
        this.id=1L;
    }

    public LastProcessedSmsId(long lastProcessedId){
        this();
        this.lastProcessedId = lastProcessedId;
    }

    @Getter
    @Setter
    @Column(name="last_processed_id")
    private Long lastProcessedId;
}