package siri.xlite.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import siri.xlite.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.MappedSuperclass;
import javax.persistence.criteria.*;
import java.sql.Time;
import java.util.List;

@MappedSuperclass
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "VehicleJourney_findByMonitoringRef",
                resultClass = VehicleJourney.class,
                query = "with recursive r_stoppoint(stoppointref, parent) as " +
                        "( " +
                        "   select s.stoppointref, s.parent" +
                        "   from stoppoint s " +
                        "   where stoppointref= :id" +
                        "union all " +
                        "   select o.stoppointref, o.parent " +
                        "   from r_stoppoint r " +
                        "   join stoppoint o ON o.parent=r.stoppointref " +
                        ") " +

                        "select * " +
                        "from vehiclejourney v " +
                        "left outer join line l on v.line_lineref=l.lineref " +
                        "inner join call c  on v.datedvehiclejourneyref=c.vehicleJourney_datedvehiclejourneyref " +
                        "left outer join stoppoint sp  on c.stopPoint_stoppointref=sp.stoppointref " +
                        "where sp.stoppointref in ( select stoppointref from r_stoppoint ) " +
                        "	and c.expecteddeparturetime>current_time " +
                        "order by c.expecteddeparturetime asc, c.index "
        )
})
@Slf4j
@ApplicationScoped
public class VehicleJourneyRepository extends ReactiveRepository<VehicleJourney, String> {

    protected VehicleJourneyRepository() {
        super(VehicleJourney.class, String.class);
    }

    public Uni<VehicleJourney> find(String id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        root.fetch(VehicleJourney_.line, JoinType.LEFT);
        root.fetch(VehicleJourney_.vias, JoinType.LEFT);
        root.fetch(VehicleJourney_.journeyParts, JoinType.LEFT);
        Fetch<VehicleJourney, Call> call = root.fetch(VehicleJourney_.calls, JoinType.LEFT);
        call.fetch(Call_.stopPoint, JoinType.LEFT);

        Predicate predicate = builder.equal(root.get(VehicleJourney_.datedVehicleJourneyRef), id);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(predicate);

        VehicleJourney result = entityManager.createQuery(criteria).getSingleResult();
        return Uni.createFrom().item(result);

    }

    public Multi<VehicleJourney> findByLineRef(String lineRef) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        Join<VehicleJourney, Line> line = (Join<VehicleJourney, Line>) root.fetch(VehicleJourney_.line,  JoinType.LEFT);
        Fetch<VehicleJourney, Call> call = root.fetch(VehicleJourney_.calls, JoinType.LEFT);
        call.fetch(Call_.stopPoint, JoinType.LEFT);

        Predicate linePredicate = builder.equal(line.get(Line_.lineRef), lineRef);
        Expression<Time> now = builder.currentTime();
        Predicate destinationExpectedArrivalTime = builder.greaterThan(root.get(VehicleJourney_.destinationExpectedArrivalTime), now);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(builder.and(linePredicate, destinationExpectedArrivalTime))
                .orderBy(builder.asc(root.get(VehicleJourney_.originExpectedDepartureTime)));

        List<VehicleJourney> result = entityManager.createQuery(criteria).getResultList();
        return Multi.createFrom().iterable(result);
    }


    public Multi<VehicleJourney> findByStopPointRefs(List<String> stopPointRefs) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        root.fetch(VehicleJourney_.line, JoinType.LEFT);
        @SuppressWarnings("unchecked")
        Join<VehicleJourney, Call> call = (Join<VehicleJourney, Call>) root.fetch(VehicleJourney_.calls);
        @SuppressWarnings("unchecked")
        Join<Call, StopPoint> stopPoint = (Join<Call, StopPoint>) call.fetch(Call_.stopPoint, JoinType.LEFT);

        Predicate stopPointPredicate = stopPoint.get(StopPoint_.stopPointRef).in(stopPointRefs);
        Expression<Time> now = builder.currentTime();
        Predicate expectedDepartureTimePredicate = builder.greaterThan(call.get(Call_.expectedDepartureTime), now);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(stopPointPredicate)
                .where(builder.and(stopPointPredicate, expectedDepartureTimePredicate))
                .orderBy(builder.asc(call.get(Call_.expectedDepartureTime)));

        List<VehicleJourney> result = entityManager.createQuery(criteria).getResultList();
        return Multi.createFrom().iterable(result);
    }

    @SuppressWarnings("unused")
    public Multi<VehicleJourney> findByMonitoringRef(String monitoringRef) {
        List<VehicleJourney> result = entityManager.createNamedQuery("VehicleJourney_findByMonitoringRef", VehicleJourney.class)
                .setParameter("id", monitoringRef)
                .getResultList();
        return Multi.createFrom().iterable(result);
    }
}
