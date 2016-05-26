package de.akquinet.jbosscc.examples.eap.jpa;

import de.akquinet.jbosscc.examples.eap.jpa.Article;
import de.akquinet.jbosscc.examples.eap.jpa.Order;
import de.akquinet.jbosscc.examples.eap.jpa.OrderItem;
import org.hibernate.jpa.QueryHints;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;

// Not directly supported in WildFly:
// https://issues.jboss.org/browse/WFLY-2727
// See persistence.xml
@DataSourceDefinition(
    name = "java:jboss/datasources/ServletSecurityDS",
    url = "jdbc:h2:mem:servlet-security;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1",
    className = "org.h2.jdbcx.JdbcDataSource",
    serverName = "localhost",
    user = "sa",
    password = "sa"
)

@Stateless
public class ArticleService {
    public static final String QUERY = "SELECT DISTINCT o FROM Order o";

    @PersistenceContext
    private EntityManager entityManager;

    public void save(final Object article) {
        entityManager.persist(article);
    }

    public List<Article> listArticles(final String name) {
        return entityManager.createQuery("SELECT a FROM Article a WHERE a.name = :name")
            .setParameter("name", name).getResultList();
    }

    public List<Order> list() {
        final List<Order> result = entityManager.createQuery(QUERY).getResultList();

        // Manual initialization, (just for demonstration, avoid LIE)

        result.forEach(o -> o.getItems().forEach(i -> i.getArticle().getColor()));
        return result;
    }

    public List<Order> listWithGraph() {
        final EntityGraph graph = entityManager.createEntityGraph(Order.GRAPH_ORDER_ITEMS);
        return entityManager.createQuery(QUERY)
            .setHint(QueryHints.HINT_FETCHGRAPH, graph)
            .getResultList();
    }

    public <T> T load(final Long id, final Class<T> clazz) {
        return entityManager.find(clazz, id);
    }

    public void addArticle(final Article article, final Long id) {
        final Order order = load(id, Order.class);
        final OrderItem item = new OrderItem();
        item.setArticle(article);
        item.setOrder(order);
        item.setQuantity(new Random().nextInt(10));
        order.getItems().add(item);

        save(article);
        save(item);
    }
}
