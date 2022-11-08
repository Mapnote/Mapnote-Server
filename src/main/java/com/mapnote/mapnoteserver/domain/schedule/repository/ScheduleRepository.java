package com.mapnote.mapnoteserver.domain.schedule.repository;

import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import java.util.Optional;
import java.util.UUID;
import javax.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

  Slice<Schedules> findByUser_IdAndScheduleStatusOrderByCreatedAtDesc(UUID userId, ScheduleStatus scheduleStatus, Pageable pageable);

}
