package ro.itschool.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private int page;

    @Column(nullable = false)
    private int price;

    private boolean isBorrowed;

    @ManyToOne
    @JsonBackReference
    private Author author;

    @ManyToMany
    @JsonBackReference
    private List<Owner> owners;

    @OneToOne
    private Owner borrower;
}
