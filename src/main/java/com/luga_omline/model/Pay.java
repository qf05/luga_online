package com.luga_omline.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)

public class Pay extends AbstractPersistable<Integer> {

    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
//    @ManyToOne(targetEntity = User.class, fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
    private int userId;

    @Column(name = "money", nullable = false)
    private double money;

    @Column(name = "pay_time", nullable = false)
    private long time;

}
