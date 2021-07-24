package com.marcinfriedrich.planningpoker.web.response;

import com.marcinfriedrich.planningpoker.domain.Size;
import com.marcinfriedrich.planningpoker.domain.User;

public class UserFullResponse {

    private final String name;
    private final boolean answered;
    private final boolean observer;
    private final Size size;

    public UserFullResponse(User user) {
        name = user.getName();
        answered = user.isAnswered();
        size = user.getSize();
        observer = user.isObserver();
    }

    public String getName() {
        return name;
    }

    public boolean isAnswered() {
        return answered;
    }

    public Size getSize() {
        return size;
    }

    public boolean isObserver() {
        return observer;
    }

}
