package com.marcinfriedrich.planningpoker.payload;

import com.marcinfriedrich.planningpoker.enums.Size;
import com.marcinfriedrich.planningpoker.model.User;

public class UserFullResponse {

    private final String name;
    private final boolean answer;
    private final boolean observer;
    private final Size size;

    public UserFullResponse(User user) {
        name = user.getName();
        answer = user.isAnswer();
        size = user.getSize();
        observer = user.isObserver();
    }

    public String getName() {
        return name;
    }

    public boolean isAnswer() {
        return answer;
    }

    public Size getSize() {
        return size;
    }

    public boolean isObserver() {
        return observer;
    }

}
