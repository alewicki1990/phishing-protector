package com.github.lewicki1990.phishingprotector.antiphishingsubscription;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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