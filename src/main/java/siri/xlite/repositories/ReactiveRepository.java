package siri.xlite.repositories;

import org.hibernate.reactive.mutiny.Mutiny;
import siri.xlite.common.Messages;

import javax.inject.Inject;
import java.util.ResourceBundle;

public abstract class ReactiveRepository<T, ID> {

    protected static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

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
