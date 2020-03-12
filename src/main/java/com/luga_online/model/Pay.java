package com.luga_online.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)

public class Pay extends AbstractPersistable<Integer> implements Serializable {

//    @Column(name = "user_id", nullable = false)
//    @Range(min = 1)
////    @ManyToOne(targetEntity = User.class, fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "money", nullable = false)
    private long money;

    @Column(name = "pay_time", nullable = false)
    private long time;
}
