package team.segroup.etms.coursesys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.coursesys.entity.Student;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String nid;
    private String name;
    private float score;

    public StudentDto(Student student) {
        this.nid = student.getNid();
        this.name = student.getName();
        this.score = student.getScore();
    }

    public Student toStudent() {
        return new Student(
            null, null,
            nid, name, score
        );
    }
}
