package com.karmanchik.chtotib_bot_rest_service.assembler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonRootName("teacher")
@Relation("teachers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherModel extends RepresentationModel<TeacherModel> {
    private Integer id;
    private String name;
}
