package com.karmanchik.chtotib_bot_rest_service.assembler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.karmanchik.chtotib_bot_rest_service.entity.enums.WeekType;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonRootName("lesson")
@Relation("lessons")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonModel extends RepresentationModel<LessonModel> {
    private Integer id;
    private Integer pairNumber;
    private Integer day;
    private String discipline;
    private String auditorium;
    private WeekType weekType;
    private GroupModel group;
    private List<TeacherModel> teachers;
}
