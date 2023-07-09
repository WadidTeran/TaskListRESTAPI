package org.kodigo.proyectos.tasklist.dtos;

import lombok.Data;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;

@Data
public class UserAndTaskDTO {
    private final UserEntity user;
    private final Task task;

    public UserAndTaskDTO(UserEntity user, Task task) {
        this.user = user;
        this.task = task;
    }


}
