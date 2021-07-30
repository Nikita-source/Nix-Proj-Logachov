package models;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "operation_time", nullable = false)
    private Instant operationTime;

    @ManyToOne
    @JoinColumn(name = "operation_category_id", referencedColumnName = "id")
    private OperationCategory operationCategory;

    public Operation() {
        this.operationTime = Instant.now();
    }

    public Operation(Double value, Account account) {
        this.value = value;
        this.account = account;
        this.operationTime = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
        verifyCategory();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Instant getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Instant operationTime) {
        this.operationTime = operationTime;
    }

    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
        verifyCategory();
    }

    private void verifyCategory() {

    }
}
