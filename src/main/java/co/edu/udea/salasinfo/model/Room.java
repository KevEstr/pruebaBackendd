package co.edu.udea.salasinfo.model;

import co.edu.udea.salasinfo.dto.response.room.RoomApplicationResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomImplementResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "room")
public class Room implements Serializable {
    @Id
    @NotNull
    @Column(name = "roomId")
    private Long id;

    @Column(name = "computerAmount", nullable = false, length = 3)
    private Integer computerAmount;

    @Column(name = "building", nullable = false, length = 16)
    private String building;

    @Column(name = "roomNum", nullable = false, length = 3)
    private String roomNum;

    @Column(name = "roomName", length = 64)
    private String roomName;

    @Column(name = "subRoom", length = 2)
    private Integer subRoom;


    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoomApplication> roomApplications;


    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoomRestriction> restrictions;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoomImplement> implementList;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room")
    private List<Reservation> reservations;
}
