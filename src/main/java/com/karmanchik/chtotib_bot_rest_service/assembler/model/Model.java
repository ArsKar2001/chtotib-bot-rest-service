package com.karmanchik.chtotib_bot_rest_service.assembler.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public abstract class Model<T extends RepresentationModel<T>> {
    protected Integer id;
}
