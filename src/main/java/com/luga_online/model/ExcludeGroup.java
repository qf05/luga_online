package com.luga_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "exlude_group", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_id", "exclude_group_id"}, name = "unique_user_exclude_group_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ExcludeGroup extends AbstractPersistable<Integer> implements Serializable {

//    @Column(name = "user_id", nullable = false)
//    @Range(min = 1)
////    @ManyToOne(targetEntity = User.class, fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "exclude_group_id", nullable = false)
    @Range(min = 1)
    private int excludeGroupId;
}
