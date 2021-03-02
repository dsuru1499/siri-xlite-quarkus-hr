package siri.xlite.repositories;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.multi.FromObservable;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.Messages;
import siri.xlite.model.Location_;
import siri.xlite.model.StopPoint;
import siri.xlite.model.StopPoint_;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.ResourceBundle;

import static org.hibernate.annotations.QueryHints.CACHEABLE;
import static siri.xlite.common.Messages.LOAD_FROM_BACKEND;
import static siri.xlite.common.OSMUtils.*;

@Slf4j
@MappedSuperclass
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "StopPoint_findByMonitoringRef",
                resultClass = StopPoint.class,
                query = "with recursive r_stoppoint(stoppointref, parent) " +
                        "as ( "+
                        "    select s.stoppointref, s.parent "+
                        "    from stoppoint s where s.stoppointref= :id "+
                        "union all "  +
                        "    select  o.stoppointref, o.parent "+
                        "    from r_stoppoint r "+
                        "   join stoppoint o on o.parent=r.stoppointref "+
                        ") " +
                        "select * from stoppoint s " +
                        "join r_stoppoint r on s.stoppointref=r.stoppointref "
                )
})
@ApplicationScoped
public class StopPointRepository extends ReactiveRepository<StopPoint, String> {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    private static RTree<String, Point> rtree;

    protected StopPointRepository() {
        super(StopPoint.class, String.class);
    }

    public Multi<StopPoint> find() {
        log.info(messages.getString(LOAD_FROM_BACKEND), "/siri-xlite/stoppoints-discovery");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StopPoint> query = builder.createQuery(type);
        Root<StopPoint> root = query.from(type);
        CriteriaQuery<StopPoint> criteria = query.select(root);
        List<StopPoint> result = entityManager.createQuery(criteria)
                .setHint(CACHEABLE, true)
                .getResultList();
        return Multi.createFrom().iterable(result);
    }

    @SuppressWarnings("unchecked")
    public Multi<StopPoint> findByLocationRTree(double[][] polygon) {
        return Uni.createFrom().item(rtree()).onItem().transformToMulti(rtree -> {
            Rectangle rectangle = Geometries.rectangleGeographic(
                    polygon[UPPER_LEFT][X], polygon[BOTTOM_RIGHT][Y],
                    polygon[BOTTOM_RIGHT][X], polygon[UPPER_LEFT][Y]);

            Observable<String> list = RxJavaInterop.toV2Observable(rtree.search(rectangle)
                    .map(Entry::value));
            return Multi.createFrom().converter(FromObservable.INSTANCE, list)
                    .onItem().transformToUniAndConcatenate(key -> {
                        StopPoint result = entityManager.find(StopPoint.class, key);
                        return Uni.createFrom().item(result);
                    });
        });
    }

    @SuppressWarnings("unused")
    public Multi<StopPoint> findByLocation(double[][] polygon) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StopPoint> query = builder.createQuery(type);
        Root<StopPoint> root = query.from(type);

        Path<Double> longitude = root.get(StopPoint_.location).get(Location_.longitude);
        Path<Double> latitude = root.get(StopPoint_.location).get(Location_.latitude);
        Predicate predicate = builder.and(
                builder.between(longitude, polygon[UPPER_LEFT][X], polygon[BOTTOM_RIGHT][X]),
                builder.between(latitude, polygon[BOTTOM_RIGHT][Y], polygon[UPPER_LEFT][Y]));

        CriteriaQuery<StopPoint> criteria = query.select(root).where(predicate);
        Iterable<StopPoint> result = entityManager.createQuery(criteria).getResultList();
        return Multi.createFrom().iterable(result);
    }

    public Multi<StopPoint> findByMonitoringRef(String monitoringRef) {
        List<StopPoint> result = entityManager.createNamedQuery("StopPoint_findByMonitoringRef", StopPoint.class)
                .setParameter("id", monitoringRef)
                .setHint(CACHEABLE, true)
                .getResultList();
        return Multi.createFrom().iterable(result);
    }

    private RTree<String, Point> rtree() {
        if (rtree == null) {
            log.info(messages.getString(LOAD_FROM_BACKEND), "rtree");
            rtree = RTree.star().maxChildren(32).create();
            Iterable<StopPoint> stopPoints = find().subscribe().asIterable();
            for (StopPoint t : stopPoints) {
                Point point = Geometries.pointGeographic(t.location().longitude().floatValue(), t.location().latitude().floatValue());
                rtree = rtree.add(t.stopPointRef(), point);
            }
        }
        return rtree;
    }

    @SuppressWarnings("unused")
    public void clear() {
        rtree = null;
    }
}