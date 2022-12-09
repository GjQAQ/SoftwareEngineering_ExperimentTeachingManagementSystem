package team.segroup.etms.coursesys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.coursesys.entity.Teacher;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private String nid;
    private String name;
    private Teacher.Role role;

    public TeacherDto(Teacher teacher) {
        this.nid = teacher.getNid();
        this.name = teacher.getName();
        this.role = teacher.getRole();
    }

    public Teacher toTeacher() {
        return new Teacher(
            null, null,
            nid, name, role
        );
    }
}
