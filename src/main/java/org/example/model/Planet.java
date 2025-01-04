package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "planets")
public class Planet {

    public Planet (String name){
        this.id=name.toUpperCase();
        this.name=name;
    }
    @Id
    @NotNull
    @Pattern(regexp = "^[A-Z0-9]+$", message = "ID must contain only uppercase Latin letters")
    @Column(name = "id", columnDefinition = "VARCHAR(10)")
    private String id;
    @NotNull
    @Size(min = 1, max = 500)
    private String name;
//comment for tests

}
