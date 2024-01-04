package com.github.lewicki1990.phishingprotector.smsprocessing.lastfetchedsmsid;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="last_fetched_sms_id")
@ToString
public class LastFetchedSmsId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    public LastFetchedSmsId(){
        this.id=1L;
    }

    public LastFetchedSmsId(long lastFetchedId){
        this();
        this.lastFetchedId = lastFetchedId;
    }

    @Getter
    @Setter
    @Column(name="last_fetched_id")
    private Long lastFetchedId;
}