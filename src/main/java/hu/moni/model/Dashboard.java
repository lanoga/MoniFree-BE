package hu.moni.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dashboard")
@Data
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "fragment")
    private String fragment;

    @Column(name = "name", nullable = false)
    private String name;

    // pl: CARD, GAUGE, LINE_CHART, BAR_CHART
    @Column(name = "visual_appering")
    private String visualAppering;

    @Column(name = "col_indx")
    private Integer colIndx;

    @Column(name = "row_indx")
    private Integer rowIndx;

    @Column(name = "color")
    private String color;
}