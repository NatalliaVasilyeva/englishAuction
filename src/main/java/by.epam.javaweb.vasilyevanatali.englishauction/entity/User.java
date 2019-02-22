package main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity;

public class User extends Bean {
    Role role;
    String login;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    int balance;
    int frozenMoney;
    int isBlocked;

    public User() {
    }

    public User(int id, Role role, String login, String firstName, String lastName, String email, String phoneNumber, int balance, int frozenMoney, int isBlocked) {
        super(id);
        this.role = role;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.frozenMoney = frozenMoney;
        this.isBlocked = isBlocked;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(int frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        boolean b = (role == user.role &&
                login.equals(user.login) &&
                lastName.equals(user.lastName) &&
                email.equals(user.email) &&
                phoneNumber.equals(user.phoneNumber) &&
                balance == user.balance) &&
                frozenMoney == user.frozenMoney &&
                isBlocked == user.isBlocked;
        return b;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (role != null ? role.hashCode() : 0);
        result = prime * result + (login != null ? login.hashCode() : 0);
        result = prime * result + (lastName != null ? lastName.hashCode() : 0);
        result = prime * result + (email != null ? email.hashCode() : 0);
        result = prime * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = prime * result + balance;
        result = prime * result + frozenMoney;
        result = prime * result + isBlocked;
        return result;
    }
}
