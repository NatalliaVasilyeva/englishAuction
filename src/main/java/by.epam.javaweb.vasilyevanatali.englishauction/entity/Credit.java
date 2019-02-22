package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Credit extends Bean{
    int userId;
    int debtSum;
    LocalDateTime recievedDate;
    LocalDateTime paymentDate;
    int creditPercent;

    public Credit() {
    }

    public Credit(int id, int userId, int debtSum, LocalDateTime recievedDate, LocalDateTime paymentDate, int creditPercent) {
        super(id);
        this.userId = userId;
        this.debtSum = debtSum;
        this.recievedDate = recievedDate;
        this.paymentDate = paymentDate;
        this.creditPercent = creditPercent;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDebtSum() {
        return debtSum;
    }

    public void setDebtSum(int debtSum) {
        this.debtSum = debtSum;
    }

    public LocalDateTime getRecievedDate() {
        return recievedDate;
    }

    public void setRecievedDate(LocalDateTime recievedDate) {
        this.recievedDate = recievedDate;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getCreditPercent() {
        return creditPercent;
    }

    public void setCreditPercent(int creditPercent) {
        this.creditPercent = creditPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Credit credit = (Credit) o;
        return userId == credit.userId &&
                debtSum == credit.debtSum &&
                creditPercent == credit.creditPercent &&
               recievedDate.equals(credit.recievedDate) &&
                paymentDate.equals(credit.paymentDate);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + userId;
        result = prime * result + debtSum;
        result = prime * result + creditPercent;
        result = prime * result + (recievedDate != null ? recievedDate.hashCode() : 0);
        result = prime * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        return result;
    }
}
