package co.edu.udea.salasinfo.model;

import co.edu.udea.salasinfo.utils.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = true)
    private User user;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "type")
    private NotificationType type; // Yes, It should be in a different table, but I refuse to manage that at this moment
}
