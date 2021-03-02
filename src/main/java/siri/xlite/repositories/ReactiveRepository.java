package siri.xlite.repositories;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class ReactiveRepository<T, ID> {

    protected Class<T> type;

    protected Class<ID> id;

    @Inject
    protected EntityManager entityManager;

    protected ReactiveRepository(Class<T> type, Class<ID> id) {
        this.type = type;
        this.id = id;
    }

}
