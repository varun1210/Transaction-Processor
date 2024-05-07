package com.transactionprocessor.repository;

import com.transactionprocessor.event_sourcing.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
}
