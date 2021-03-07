package siri.xlite.repositories;

import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.model.Line;
import siri.xlite.model.Line_;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import static siri.xlite.common.Messages.LOAD_FROM_BACKEND;

@Slf4j
@ApplicationScoped
public class LineRepository extends ReactiveRepository<Line, String> {

    protected LineRepository() {
        super(Line.class, String.class);
    }

    public Multi<Line> find() {
        log.info(messages.getString(LOAD_FROM_BACKEND), type);

        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<Line> query = builder.createQuery(type);
        Root<Line> root = query.from(type);
        root.fetch(Line_.destinations, JoinType.LEFT);

        CriteriaQuery<Line> criteria = query.select(root).distinct(true);
        return session.createQuery(criteria).setCacheable(false).getResults();
    }

}
