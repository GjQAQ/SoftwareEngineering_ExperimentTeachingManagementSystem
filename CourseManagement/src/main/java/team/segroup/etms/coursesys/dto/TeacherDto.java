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
}
