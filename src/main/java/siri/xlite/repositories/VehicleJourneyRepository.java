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

import static siri.xlite.common.Messages.LOAD_FROM_BACKEND;

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
        log.info(messages.getString(LOAD_FROM_BACKEND), type);

        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        Fetch<VehicleJourney, Line> line = root.fetch(VehicleJourney_.line);
        Fetch<VehicleJourney, Via> via = root.fetch(VehicleJourney_.vias, JoinType.LEFT);
        Fetch<VehicleJourney, JourneyPart> journeyPart = root.fetch(VehicleJourney_.journeyParts, JoinType.LEFT);
        Fetch<VehicleJourney, Call> call = root.fetch(VehicleJourney_.calls, JoinType.LEFT);
        Fetch<Call, StopPoint> stopPoint = call.fetch(Call_.stopPoint);

        Predicate predicate = builder.equal(root.get(VehicleJourney_.datedVehicleJourneyRef), id);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(predicate);

        return session.createQuery(criteria).getSingleResult();

    }

    public Multi<VehicleJourney> findByLineRef(String lineRef) {
        log.info(messages.getString(LOAD_FROM_BACKEND), type);

        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        Fetch<VehicleJourney, Line> line = root.fetch(VehicleJourney_.line);
        Fetch<VehicleJourney, Call> call = root.fetch(VehicleJourney_.calls, JoinType.LEFT);
        Fetch<Call, StopPoint> stopPoint = call.fetch(Call_.stopPoint);

        Predicate linePredicate = builder.equal(root.get(VehicleJourney_.line), lineRef);
        Expression<Time> now = builder.currentTime();
        Predicate destinationExpectedArrivalTime = builder.greaterThan(root.get(VehicleJourney_.destinationExpectedArrivalTime), now);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(builder.and(linePredicate, destinationExpectedArrivalTime))
                .orderBy(builder.asc(root.get(VehicleJourney_.originExpectedDepartureTime)));

        return session.createQuery(criteria).getResults();
    }


    public Multi<VehicleJourney> findByStopPointRefs(List<String> stopPointRefs) {
        log.info(messages.getString(LOAD_FROM_BACKEND), type);

        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<VehicleJourney> query = builder.createQuery(type);

        Root<VehicleJourney> root = query.from(type);
        Join<VehicleJourney, Line> line = (Join<VehicleJourney, Line>) root.fetch(VehicleJourney_.line, JoinType.LEFT);
        Join<VehicleJourney, Call> call = (Join<VehicleJourney, Call>) root.fetch(VehicleJourney_.calls);
        Join<Call, StopPoint> stopPoint = (Join<Call, StopPoint>) call.fetch(Call_.stopPoint, JoinType.LEFT);

        Predicate stopPointPredicate = stopPoint.get(StopPoint_.stopPointRef).in(stopPointRefs);
        Expression<Time> now = builder.currentTime();
        Predicate expectedDepartureTimePredicate = builder.greaterThan(call.get(Call_.expectedDepartureTime), now);

        CriteriaQuery<VehicleJourney> criteria = query.select(root).distinct(true)
                .where(stopPointPredicate)
                .where(builder.and(stopPointPredicate, expectedDepartureTimePredicate))
                .orderBy(builder.asc(call.get(Call_.expectedDepartureTime)));

        return session.createQuery(criteria).getResults();
    }

    @SuppressWarnings("unused")
    public Multi<VehicleJourney> findByMonitoringRef(String monitoringRef) {
        log.info(messages.getString(LOAD_FROM_BACKEND), type);

        return session.createNamedQuery("VehicleJourney_findByMonitoringRef", VehicleJourney.class)
                .setParameter("id", monitoringRef)
                .getResults();
    }
}
