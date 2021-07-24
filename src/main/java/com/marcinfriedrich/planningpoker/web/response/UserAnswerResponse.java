package com.marcinfriedrich.planningpoker.web.response;

import com.marcinfriedrich.planningpoker.domain.User;

public class UserAnswerResponse {

    private final String name;
    private final boolean answered;
    private final boolean observer;

    public UserAnswerResponse(User user) {
        this.name = user.getName();
        this.answered = user.isAnswered();
        this.observer = user.isObserver();
    }

    public String getName() {
        return name;
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isObserver() {
        return observer;
    }

}
