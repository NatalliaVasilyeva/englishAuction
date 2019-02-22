package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

public enum AuctionType {

    DIRECT("direct"),
    REVERSE("reverse");

    private String name;

    AuctionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
