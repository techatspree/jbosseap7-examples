package de.akquinet.jbosscc.examples.eap.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "JUMBLE_ORDER_ITEM")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue
    @Id
    private Long id;

    @Column
    private int quantity;

    @ManyToOne
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(final Article article) {
        this.article = article;
    }
}
