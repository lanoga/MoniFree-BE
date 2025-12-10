package hu.moni.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "monitor_endpoint")
@Data
public class MonitorEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private ApplicationData application;

    // Pl: MEMORY, CPU, HDD, UPTIME
    @Column(name = "fragment", nullable = false)
    private String fragment;

    // másodperc
    @Column(name = "interval_sec", nullable = false)
    private Integer intervalSec;

}

