package de.akquinet.jbosscc.examples.eap.jpa;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.Random;

@Singleton
@Named
public class ArticleController {
    @EJB
    private ArticleService articleService;

    private String articleName = "Book";

    private Order order = new Order();

    @PostConstruct
    public void init() {
        articleService.save(order);
    }

    public void createArticle() {
        final Article article = new Article();
        article.setName(getArticleName());
        article.setColor(getColor());

        articleService.addArticle(article, order.getId());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Article created: " + article.getId(), null));
    }

    private Color getColor() {
        switch (new Random().nextInt(3)) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            default:
                return Color.RED;
        }
    }

    public List<Order> getOrders() {
        return articleService.list();
    }


    public List<Order> getOrdersDeep() {
        return articleService.listWithGraph();
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }
}
