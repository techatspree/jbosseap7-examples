package de.akquinet.jbosscc.examples.eap.jpa;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import java.util.List;

public class MyListener {
    @EJB
    private ArticleService articleService;

    @PrePersist
    public void prePersist(final Article article) {
        final String name = article.getName();
        final List<Article> articles = articleService.listArticles(name);

        System.out.println("Existing articles with name " + name  + ": " + articles);
    }
}
