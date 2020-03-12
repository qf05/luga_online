package com.luga_online.to;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserTo {

    private String name;
    private String photo;
    private String money;
}
