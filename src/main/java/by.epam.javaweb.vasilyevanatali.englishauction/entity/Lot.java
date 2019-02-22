package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lot extends Bean {
    String name;
    String description;
    Category category;
    int auctionId;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    int sellerId;
    private String sellerEmail;
    private String sellerLogin;
    private int sellerIsBlocked;
    int startPrice;
    int minimumStep;
    boolean isPaid;
    int isBlocked;
    private int bidId;
    private int bidderId;
    private int bidPrice;


    public Lot() {
    }

    public Lot(int id, String name, String description, Category category, int auctionId, int sellerId, int startPrice, int minimumStep, boolean isPaid, int isBlocked) {
        super(id);
        this.name = name;
        this.description = description;
        this.category = category;
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.startPrice = startPrice;
        this.minimumStep = minimumStep;
        this.isPaid = isPaid;
        this.isBlocked = isBlocked;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getMinimumStep() {
        return minimumStep;
    }

    public void setMinimumStep(int minimumStep) {
        this.minimumStep = minimumStep;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
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

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerLogin() {
        return sellerLogin;
    }

    public void setSellerLogin(String sellerLogin) {
        this.sellerLogin = sellerLogin;
    }

    public int isSellerIsBlocked() {
        return sellerIsBlocked;
    }

    public void setSellerIsBlocked(int sellerIsBlocked) {
        this.sellerIsBlocked = sellerIsBlocked;
    }

    public int getSellerIsBlocked() {
        return sellerIsBlocked;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getBidderId() {
        return bidderId;
    }

    public void setBidderId(int bidderId) {
        this.bidderId = bidderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lot lot = (Lot) o;
        return category.equals(lot.category) &&
                auctionId == lot.auctionId &&
                sellerId == lot.sellerId &&
                startPrice == lot.startPrice &&
                minimumStep == lot.minimumStep &&
                isPaid == lot.isPaid &&
                isBlocked == lot.isBlocked &&
                name.equals(lot.name) &&
                description.equals(lot.description) &&
                startTime.equals(lot.startTime) &&
                finishTime.equals(lot.finishTime) &&
                sellerEmail.equalsIgnoreCase(lot.sellerEmail) &&
                sellerLogin.equals(lot.sellerLogin) &&
                sellerIsBlocked == lot.sellerIsBlocked &&
                bidId == lot.bidId &&
                bidderId == lot.bidderId &&
                bidPrice == lot.bidPrice;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (category != null ? category.hashCode() : 0);
        result = prime * result + auctionId;
        result = prime * result + sellerId;
        result = prime * result + startPrice;
        result = prime * result + minimumStep;
        result = prime * result + (isPaid ? 1231 : 1237);
        result = prime * result + isBlocked;
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (description != null ? description.hashCode() : 0);
        result = prime * result + (startTime != null ? startTime.hashCode() : 0);
        result = prime * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = prime * result + (sellerEmail != null ? sellerEmail.hashCode() : 0);
        result = prime * result + (sellerLogin != null ? sellerLogin.hashCode() : 0);
        result = prime * result + sellerIsBlocked;
        result = prime * result + bidId;
        result = prime * result + bidderId;
        result=prime*result+bidPrice;
        return result;
    }
}
