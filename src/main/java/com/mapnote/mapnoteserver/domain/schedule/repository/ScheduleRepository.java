package com.mapnote.mapnoteserver.domain.schedule.repository;

import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

  Slice<Schedules> findByUser_IdAndScheduleStatusOrderByCreatedAtDesc(UUID userId, ScheduleStatus scheduleStatus, Pageable pageable);

  Optional<Schedules> findByIdAndUser_Id(Long id, UUID userId);

  @Query(value="SELECT s FROM Schedules s "
      + "WHERE s.user.id=:userId AND "
      + "6371*cos(radians(37.409))*cos(radians(:lat))*cos(radians(:lot))-cos(radians(127.128))"
      + "+sin(radians(37.409))*sin(radians(:lat)) <= s.user.boundary")
  List<Schedules> findByRadius(@Param("userId") UUID userId,@Param("lat") Double lat, @Param("lot") Double lot);

}
