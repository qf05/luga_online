package com.luga_online.model;


import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)

public class User {

    @Id
    @Column(name = "vk_id", nullable = false, unique = true)
    @Range(min = 1)
//    @OneToMany(targetEntity = )
    private int vkId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "tel", nullable = false)
    @NotEmpty
    @Size(max = 18, min = 18)
    private String tel;

    @Column(name = "money", nullable = false)
    private double money;

}
