package com.villageconnect.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_timings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShopTiming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    @Column(name = "is_holiday")
    private Boolean isHoliday = false;

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}