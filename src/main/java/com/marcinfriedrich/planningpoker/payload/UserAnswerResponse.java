package com.marcinfriedrich.planningpoker.payload;

import com.marcinfriedrich.planningpoker.model.User;

public class UserAnswerResponse {

    private final String name;
    private final boolean answer;
    private final boolean observer;

    public UserAnswerResponse(User user) {
        this.name = user.getName();
        this.answer = user.isAnswer();
        this.observer = user.isObserver();
    }

    public String getName() {
        return name;
    }

    public boolean isAnswer() {
        return answer;
    }

    public boolean isObserver() {
        return observer;
    }

}
