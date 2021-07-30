package models;

import java.time.Instant;

public class OperationInfo {
    private String user;
    private Long accountId;
    private Long operationId;
    private Double money;
    private Instant operationTime;

    public OperationInfo() {

    }

    public OperationInfo(String user, Long accountId, Long operationId, Double money, Instant operationTime) {
        this.user = user;
        this.accountId = accountId;
        this.operationId = operationId;
        this.money = money;
        this.operationTime = operationTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getAccount() {
        return accountId;
    }

    public void setAccount(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Instant getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Instant operationTime) {
        this.operationTime = operationTime;
    }
}
