package de.akquinet.jbosscc.examples.eap.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "JUMBLE_ORDER")
@NamedEntityGraph(name = Order.GRAPH_ORDER_ITEMS,
    attributeNodes = @NamedAttributeNode(value = "items", subgraph = "itemsGraph"),
    subgraphs = @NamedSubgraph(name = "itemsGraph",
        attributeNodes = @NamedAttributeNode("article")))
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String GRAPH_ORDER_ITEMS = "graph.Order.items";

    @GeneratedValue
    @Id
    private Long id;

    @Column
    private String orderNumber;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private final Set<OrderItem> items = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<OrderItem> getItems() {
        return items;
    }
}
