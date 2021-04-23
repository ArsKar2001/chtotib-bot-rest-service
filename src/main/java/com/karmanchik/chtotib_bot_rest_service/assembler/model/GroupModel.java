package com.karmanchik.chtotib_bot_rest_service.assembler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

/**
 * Представляет web-модель сущности - Group
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("group")
@Relation("groups")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class GroupModel extends RepresentationModel<GroupModel> {
    private Integer id;
    private String name;
}


