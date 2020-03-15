package com.luga_online.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Group implements Serializable {

    @Id
    @Column(name = "group_id", nullable = false, unique = true)
    @Range(min = 1)
    private Integer groupId;

    @Range(min = 1)
    private long price;

    @Column(name = "limit_invited")
    @Range(min = 0)
    private Integer limitInvited;

    @Column(name = "all_invited")
    @Range(min = 0)
    private Integer allInvited;

    private boolean active;

    private String cities;

    private Integer sex;

    @Column(name = "min_Age")
    @Range(min = 12, max = 86)
    private Integer minAge;

    @Column(name = "max_Age")
    @Range(min = 12, max = 86)
    private Integer maxAge;

}
