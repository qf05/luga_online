package com.luga_online.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Group {

    @Id
    @Column(name = "group_id", nullable = false, unique = true)
    @Range(min = 1)
    private Integer groupId;

    @Column(name = "limit_invited")
    @Range(min = 0)
    private Integer limitInvited;

    @Column(name = "all_invited")
    private Integer allInvited;

    private boolean active;
}
