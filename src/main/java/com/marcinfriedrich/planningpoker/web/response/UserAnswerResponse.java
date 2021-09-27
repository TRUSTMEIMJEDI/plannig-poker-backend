package com.marcinfriedrich.planningpoker.web.response;

import com.marcinfriedrich.planningpoker.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode
public class UserAnswerResponse {
    String name;
    boolean answered;
    boolean observer;

    public static UserAnswerResponse of(User user) {
        return new UserAnswerResponse(user.getName(), user.isAnswered(), user.isObserver());
    }
}
