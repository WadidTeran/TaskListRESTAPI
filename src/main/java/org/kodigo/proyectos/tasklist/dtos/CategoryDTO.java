package org.kodigo.proyectos.tasklist.dtos;

import lombok.Data;
import org.kodigo.proyectos.tasklist.entities.User;

@Data
public class CategoryDTO {
    private Long categoryId;

    private String name;

    private User user;
}
