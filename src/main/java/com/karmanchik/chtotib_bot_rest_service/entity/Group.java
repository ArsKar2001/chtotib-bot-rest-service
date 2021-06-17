package com.karmanchik.chtotib_bot_rest_service.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name = "groups")
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "hiddenBuilder")
public class Group extends BaseEntity {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<ChatUser> chatUsers;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Replacement> replacements;

    public Group() { }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

