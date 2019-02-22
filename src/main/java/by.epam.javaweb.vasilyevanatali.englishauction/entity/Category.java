package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

public enum Category {
    CAR("car"), PICTURE("picture"), COMPUTER("computer"), PHONE("phone");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    }
