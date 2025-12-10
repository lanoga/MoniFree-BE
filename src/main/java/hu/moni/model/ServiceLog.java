package hu.moni.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "service_log")
public class ServiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "result_code")
    private Integer resultCode;

    @Column(length = 2000)
    private String errorMessage;
}