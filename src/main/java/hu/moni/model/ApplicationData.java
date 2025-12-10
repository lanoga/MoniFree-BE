package hu.moni.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "application_data")
public class ApplicationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "auth_user")
    private String user;

    @Column(name = "auth_password")
    private String pass;

    @OneToMany(mappedBy = "application", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<MonitorEndpoint> endpoints;
}
