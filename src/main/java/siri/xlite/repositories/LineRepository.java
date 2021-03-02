package siri.xlite.repositories;

import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.Messages;
import siri.xlite.model.Line;
import siri.xlite.model.Line_;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.ResourceBundle;

import static org.hibernate.annotations.QueryHints.CACHEABLE;
import static siri.xlite.common.Messages.LOAD_FROM_BACKEND;

@Slf4j
@ApplicationScoped
public class LineRepository extends ReactiveRepository<Line, String> {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    protected LineRepository() {
        super(Line.class, String.class);
    }

    public Multi<Line> find() {
        log.info(messages.getString(LOAD_FROM_BACKEND), "/siri-xlite/lines-discovery");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Line> query = builder.createQuery(type);
        Root<Line> root = query.from(type);
        root.fetch(Line_.destinations, JoinType.LEFT);

        CriteriaQuery<Line> criteria = query.select(root).distinct(true);
        List<Line> result = entityManager.createQuery(criteria)
                .setHint(CACHEABLE, true)
                .getResultList();

        return Multi.createFrom().iterable(result);
    }

}
