package co.edu.udea.salasinfo.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "roomsoftware")
public class RoomApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicationId", nullable = false)
    private Application application;

    @Column(name = "version", nullable = false)
    private String version; // Atributo adicional
}
