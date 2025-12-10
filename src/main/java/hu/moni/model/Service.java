package hu.moni.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_url")
    private String serverUrl;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "fragment")
    private String fragment;

    @Column(name = "name")
    private String name;

    @Column(name = "interval_sec")
    private Integer intervalSec;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
