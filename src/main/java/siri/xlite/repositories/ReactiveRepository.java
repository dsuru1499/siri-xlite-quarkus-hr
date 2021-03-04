package siri.xlite.repositories;

import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;

public abstract class ReactiveRepository<T, ID> {

    protected Class<T> type;
    protected Class<ID> id;

    @Inject
    Mutiny.SessionFactory factory;

    @Inject
    Mutiny.Session session;

    protected ReactiveRepository(Class<T> type, Class<ID> id) {
        this.type = type;
        this.id = id;
    }

}
