package db2.todolistapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="task")
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    private int storyPoints;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

}
