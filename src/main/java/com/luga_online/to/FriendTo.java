package com.luga_online.to;

import com.luga_online.model.Group;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class FriendTo {
    private int id;
    private String photo;
    private String name;
    private List<Group> groups;
}
