package com.github.lewicki1990.phishingprotector.smsprocessing.lastfetchedsmsid;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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