package hu.moni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    private Long id;
    private Long applicationId;
    private String fragment;
    private String name;
    private String visualAppering;
    private Integer colIndx;
    private Integer rowIndx;
    private String color;


    private List<ServiceResultDto> data;
}

