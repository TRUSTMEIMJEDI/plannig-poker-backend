package com.marcinfriedrich.planningpoker.web.response;

import com.marcinfriedrich.planningpoker.domain.Size;
import com.marcinfriedrich.planningpoker.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode
public class UserFullResponse {
    String name;
    boolean answered;
    boolean observer;
    Size size;

    public static UserFullResponse of(User user) {
        return new UserFullResponse(
                user.getName(),
                user.isAnswered(),
                user.isObserver(),
                user.getSize());
    }
}
