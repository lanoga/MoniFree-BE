package hu.moni.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "service_result")
public class ServiceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "CLOB")
    private String result;

    @Column(name = "fragment")
    private String fragment;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "visualize")
    private String visualize;
}