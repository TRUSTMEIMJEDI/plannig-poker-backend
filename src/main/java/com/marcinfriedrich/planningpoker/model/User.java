package com.marcinfriedrich.planningpoker.model;

import com.marcinfriedrich.planningpoker.enums.Size;
import com.marcinfriedrich.planningpoker.util.RandomUtil;

public class User {
    private final String key;
    private String name;
    private Size size;
    private boolean answer;
    private boolean owner = false;
    private boolean observer = false;

    public User(String name) {
        this.key = RandomUtil.generateUserKey();
        this.name = name;
        this.size = null;
        this.answer = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean isObserver() {
        return observer;
    }

    public void setObserver(boolean observer) {
        this.observer = observer;
    }
}
