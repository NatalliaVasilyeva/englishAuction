package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

public class Bid extends Bean {
    int bidAmount;
    int userId;
    int lotId;

    public Bid() {
    }

    public Bid(int id, int bidAmount, int userId, int lotId) {
        super(id);
        this.bidAmount = bidAmount;
        this.userId = userId;
        this.lotId = lotId;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bid bid = (Bid) o;
        return bidAmount == bid.bidAmount &&
                userId == bid.userId &&
                lotId == bid.lotId;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + bidAmount;
        result = prime * result + userId;
        result = prime * result + lotId;
        return result;
    }
}
