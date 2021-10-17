package com.assessment.aneeque.payload.response;

import com.assessment.aneeque.model.User;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ListOfUsersResponse {
    private List<User> users;
}
