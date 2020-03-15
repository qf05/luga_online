package com.luga_online.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "invite", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_id", "invited_id"}, name = "unique_user_invited_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Invite extends AbstractPersistable<Integer> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "group_id", nullable = false)
    private int groupId;

    @Column(name = "invited_id", nullable = false)
    @Range(min = 1)
    private int invitedId;

    @Column(name = "result", nullable = false)
    private int result;

    @Column(name = "invite_time", nullable = false)
    private long time;
}
