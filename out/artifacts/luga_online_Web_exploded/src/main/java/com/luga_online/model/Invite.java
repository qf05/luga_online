package com.luga_online.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "invite", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_id", "invited_id"}, name = "unique_user_invited_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Invite extends AbstractPersistable<Integer> {

    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
//    @ManyToOne(targetEntity = User.class, fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
    private String userId;

    @Column(name = "invited_id", nullable = false)
    @Range(min = 1)
    private int invitedId;

    @Column(name = "invite_time", nullable = false)
    private long time;
}
