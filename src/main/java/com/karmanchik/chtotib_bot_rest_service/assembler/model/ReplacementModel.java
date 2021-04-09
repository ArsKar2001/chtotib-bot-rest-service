package com.karmanchik.chtotib_bot_rest_service.assembler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonRootName("replacement")
@Relation("replacements")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplacementModel extends RepresentationModel<ReplacementModel> {
    private Integer id;

    private String pairNumber;
    private String discipline;
    private String auditorium;
    private LocalDate date;

    private GroupModel group;
    private List<TeacherModel> teachers;
}
