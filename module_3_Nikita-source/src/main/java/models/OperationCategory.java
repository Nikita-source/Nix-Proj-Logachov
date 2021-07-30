package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "operations_category")
public class OperationCategory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category")
    private Category category;

    //@OneToMany(mappedBy = "operation_category", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Operation> operations;

    public OperationCategory() {
        //this.operations = new ArrayList<>();
    }

    public OperationCategory(String name, Category category) {
        this.name = name;
        this.category = category;
        //this.operations = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /*public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }*/
}
