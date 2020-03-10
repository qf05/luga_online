package com.luga_online.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "exlude_group", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_id", "exclude_group_id"}, name = "unique_user_exclude_group_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ExcludeGroup extends AbstractPersistable<Integer> {

    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
//    @ManyToOne(targetEntity = User.class, fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Integer userId;

    @Column(name = "exclude_group_id", nullable = false)
    @Range(min = 1)
    private int excludeGroupId;
}
