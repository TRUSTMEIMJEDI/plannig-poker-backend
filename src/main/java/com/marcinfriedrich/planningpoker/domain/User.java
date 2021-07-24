package com.marcinfriedrich.planningpoker.domain;

import com.marcinfriedrich.planningpoker.util.RandomUtil;
import lombok.Data;

@Data
public class User {
    private final String id;
    private String name;
    private Size size;
    private boolean answered = false;
    private boolean owner = false;
    private boolean observer;

    private User(String name, boolean observer) {
        this.id = RandomUtil.generateUserKey();
        this.name = name;
        this.size = null;
        this.observer = observer;
    }

    public static User of(String name, boolean observer) {
        return new User(name, observer);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", answered=" + answered +
                ", owner=" + owner +
                ", observer=" + observer +
                '}';
    }
}
