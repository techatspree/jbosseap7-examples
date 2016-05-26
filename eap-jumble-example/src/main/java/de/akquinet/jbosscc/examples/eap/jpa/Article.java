package de.akquinet.jbosscc.examples.eap.jpa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.io.Serializable;

@Entity
@Table(name = "JUMBLE_ARTIKEL")
@EntityListeners(MyListener.class)
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue
    @Id
    private Long id;

    @Basic
    @NotNull
    @Column(name = "NAME")
    @Size(min = 1, max = 25)
    private String name;

    @Column(name = "COLOR")
    @Convert(converter = ColorConverter.class)
    private Color color;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }
}
