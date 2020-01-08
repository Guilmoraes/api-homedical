package br.com.homedical.web.rest;

import br.com.homedical.facade.ScheduleFacade;
import br.com.homedical.facade.dto.MobileScheduleDTO;
import br.com.homedical.facade.dto.ScheduleDTO;
import br.com.homedical.facade.dto.specialty.ScheduleCompleteReportDTO;
import br.com.homedical.facade.dto.specialty.ScheduleReportDTO;
import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.web.rest.util.PaginationUtil;
import br.com.homedical.web.rest.vm.ListSchedulesVM;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    private final ScheduleFacade scheduleFacade;

    public ScheduleResource(ScheduleFacade scheduleFacade) {
        this.scheduleFacade = scheduleFacade;
    }

    @PostMapping("/schedules/list")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<List<ScheduleDTO>> createListSchedule(@Valid @RequestBody ListSchedulesVM scheduleDTO) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", scheduleDTO);
        List<ScheduleDTO> result = scheduleFacade.saveListSchedules(scheduleDTO.getSchedules());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/schedules")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity createSchedule(@Valid @RequestBody ScheduleDTO schedule) throws URISyntaxException {
        ScheduleDTO result = scheduleFacade.saveSchedule(schedule);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/schedules/approved")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity approvedSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) throws URISyntaxException {
        ScheduleDTO result = scheduleFacade.approvedSchedules(scheduleDTO);
        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/schedules/{id}/pending")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<ScheduleDTO>> listSchedulesPendingForProfessional(@PathVariable String id) {
        List<ScheduleDTO> result = scheduleFacade.listSchedulesPendingForProfessional(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/schedules/pending")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<List<ScheduleDTO>> listSchedulePending() {
        List<ScheduleDTO> result = scheduleFacade.listSchedulePending();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/schedules/{id}/report")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<Page<ScheduleReportDTO>> filterScheduleForDate(@PathVariable String id,
                                                                         @RequestParam(name = "start") ZonedDateTime start,
                                                                         @RequestParam(name = "finish") ZonedDateTime finish,
                                                                         @ApiParam Pageable pageable) {
        Page<ScheduleReportDTO> schedule = scheduleFacade.filterScheduleForDate(id, start, finish, pageable);
        return ResponseEntity.ok().body(schedule);
    }

    @GetMapping("/schedules/{id}/complete-report")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public ResponseEntity<ScheduleCompleteReportDTO> getScheduleCompleteReport(@PathVariable String id) {
        ScheduleCompleteReportDTO schedule = scheduleFacade.getScheduleCompleteReport(id);
        return ResponseEntity.ok().body(schedule);
    }

    @GetMapping("/schedules")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<Page<ScheduleDTO>> getAllSchedules(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Schedules");
        Page<ScheduleDTO> page = scheduleFacade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedules");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/schedules/{id}/professional")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<List<MobileScheduleDTO>> listProfessionalSchedules(@PathVariable String id) {
        return ResponseEntity.ok().body(scheduleFacade.listProfessionalSchedules(id));
    }

    @GetMapping("/schedules/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.PROFESSIONAL})
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable String id) {
        log.debug("REST request to get Schedule : {}", id);
        ScheduleDTO scheduleDTO = scheduleFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(scheduleDTO));
    }

}
