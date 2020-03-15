package com.luga_online.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {

    @Id
    @Column(name = "vk_id", nullable = false, unique = true)
    @Range(min = 1)
    private Integer vkId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "tel")
//    @NotEmpty
//    @Size(max = 18, min = 18)
    private String tel;

    @Column(name = "money", nullable = false)
    private long money;

    private boolean banned;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<ExcludeGroup> excludeGroups;

    @Override
    public String toString() {
        return "User{" +
                "vkId=" + vkId +
                ", role=" + role +
                ", tel='" + tel + '\'' +
                ", money=" + money +
                ", banned=" + banned +
                '}';
    }
}
