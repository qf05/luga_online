package com.luga_online.to;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class GroupTo {
    private int id;
    private String name;
    private String price;
    private String photo;
}
