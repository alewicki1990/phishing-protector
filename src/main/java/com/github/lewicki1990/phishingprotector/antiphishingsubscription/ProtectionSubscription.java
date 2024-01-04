package com.github.lewicki1990.phishingprotector.antiphishingsubscription;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "protection_subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProtectionSubscription {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column
    private long msisdn;

    @Column(name = "is_active")
    private boolean isActive;

    @Getter
    @Column(name = "change_date")
    private LocalDateTime changeDate;

    public void setId(Long id) {
        this.id = id;
    }

    public void setMsisdn(long msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        isActive = true;
    }

    public void setInactive() {
        isActive = false;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }


}