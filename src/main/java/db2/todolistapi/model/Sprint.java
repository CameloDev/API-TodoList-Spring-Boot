package db2.todolistapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Table(name = "sprint")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<TaskItem> tasks;

    @Override
    public String toString() {
        return "Sprint " + id;
    }


}

