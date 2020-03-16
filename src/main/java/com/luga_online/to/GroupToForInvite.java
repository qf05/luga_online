package com.luga_online.to;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class GroupToForInvite {
    private int id;
    private String name;
    private String photo;
    private String message;
}
