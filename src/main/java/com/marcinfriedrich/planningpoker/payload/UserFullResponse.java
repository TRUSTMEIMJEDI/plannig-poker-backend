package com.marcinfriedrich.planningpoker.payload;

import com.marcinfriedrich.planningpoker.enums.Size;
import com.marcinfriedrich.planningpoker.model.User;

public class UserFullResponse {
    private final String name;
    private final boolean answer;
    private final Size size;

    public UserFullResponse(User user) {
        this.name = user.getName();
        this.answer = user.isAnswer();
        this.size = user.getSize();
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
}
