package com.mapnote.mapnoteserver.domain.schedule.repository;

import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedules, UUID> {

}
