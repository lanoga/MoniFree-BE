package hu.moni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ServiceResultDto {
    //Frontra ez megy vissza hogy rendes Json-t kapjon
    private Long id;
    private String serviceName;
    private Long serviceId;
    private Object result;
    private String fragment;
    private LocalDateTime dateTime;
}
