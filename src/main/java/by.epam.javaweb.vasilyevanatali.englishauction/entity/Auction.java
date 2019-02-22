package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Auction extends Bean {
    LocalDateTime startTime;
    LocalDateTime finishTime;
    AuctionType auctionType;
    String description;

    List<Lot> lots = new ArrayList<>();

    public Auction() {
    }

    public Auction(int id, LocalDateTime startTime, LocalDateTime finishTime, AuctionType auctionType, String description) {
        super(id);
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.auctionType = auctionType;
        this.description = description;
    }

    public Auction(int id, LocalDateTime startTime, LocalDateTime finishTime, AuctionType auctionType) {
        super(id);
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.auctionType = auctionType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Auction auction = (Auction) o;
        boolean b = (auctionType == auction.auctionType &&
                startTime.equals(auction.startTime) &&
                finishTime.equals(auction.finishTime) &&
                description.equals(auction.description)) &&
                lots.equals(auction.lots);
        return b;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (auctionType != null ? auctionType.hashCode() : 0);
        result = prime * result + (startTime != null ? startTime.hashCode() : 0);
        result = prime * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = prime * result + (description != null ? description.hashCode() : 0);
        result = prime * result + (lots != null ? lots.hashCode() : 0);
        return result;
    }
}
