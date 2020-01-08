package br.com.homedical.service.impl;

import br.com.homedical.domain.Professional;
import br.com.homedical.domain.Schedule;
import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.event.FileEvent;
import br.com.homedical.repository.ProfessionalRepository;
import br.com.homedical.repository.ScheduleRepository;
import br.com.homedical.repository.impl.ScheduleQueryRepository;
import br.com.homedical.service.ScheduleService;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;

    private final ApplicationEventPublisher publisher;

    private final ProfessionalRepository professionalRepository;

    private final ScheduleQueryRepository scheduleQueryRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               ApplicationEventPublisher publisher,
                               ProfessionalRepository professionalRepository,
                               ScheduleQueryRepository scheduleQueryRepository) {
        this.scheduleRepository = scheduleRepository;
        this.publisher = publisher;
        this.professionalRepository = professionalRepository;
        this.scheduleQueryRepository = scheduleQueryRepository;
    }

    @Override
    public List<Schedule> saveListSchedules(List<Schedule> schedule) {
        log.debug("Request to save list of Schedules : {}", schedule);
        List<Schedule> savedSchedules = scheduleRepository.save(schedule);
        savedSchedules.stream()
            .findFirst()
            .ifPresent(it -> {
                boolean hasImageToUpload = (
                    it.getSignatures() != null &&
                        it.getScheduleImages().size() == 0 &&
                        it.getSignatures()
                            .stream()
                            .anyMatch(file -> file.getImage() != null && file.getImage().getFile() != null));

                if (hasImageToUpload) {
                    List<String> signatureFilesIds = it.getSignatures()
                        .stream()
                        .map(file -> file.getImage().getId())
                        .collect(Collectors.toList());
                    if(signatureFilesIds.size() > 0){
                        publisher.publishEvent(new FileEvent.StartFilesUploadByIds(signatureFilesIds));
                    }
                }
            });
        return savedSchedules;
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        log.debug("Request to save Schedules : {}", schedule);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        boolean hasImageToUpload = (
            schedule.getSignatures() != null &&
            schedule.getSignatures()
                .stream()
                .anyMatch(file -> file.getImage() != null && file.getImage().getFile() != null));

        if (hasImageToUpload) {
            List<String> signatureFilesIds = schedule.getSignatures()
                .stream()
                .map(it -> it.getImage().getId())
                .collect(Collectors.toList());

            if(signatureFilesIds.size() > 0){
                publisher.publishEvent(new FileEvent.StartFilesUploadByIds(signatureFilesIds));
            }
        }

        return savedSchedule;
    }

    @Override
    public Schedule approvedSchedules(Schedule schedule) {
        checkSchedulesPending(schedule);
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> listSchedulesPendingForProfessional(String id) {
        log.debug("Request list of schedules with status pending for Professional");
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_PROFESSIONAL_NOT_FOUND));
        return scheduleQueryRepository.listSchedulesPendingForProfessional(professional.getId());
    }

    @Override
    public List<Schedule> listSchedulePending() {
        log.debug("Request list of schedules with status pending");
        return scheduleQueryRepository.listSchedulePending();
    }


    public List<Schedule> listProfessionalSchedules(String id) {
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_PROFESSIONAL_NOT_FOUND));
        return scheduleRepository.findByProfessionalId(professional.getId());
    }

    @Override
    public Page<Schedule> filterScheduleForDate(String id, ZonedDateTime start, ZonedDateTime finish, Pageable pageable) {
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_PROFESSIONAL_NOT_FOUND));
        return scheduleQueryRepository.filterScheduleForDate(professional.getId(), start, finish, pageable);
    }

    private void checkSchedulesPending(Schedule schedule) {
        Schedule exist = scheduleRepository.findById(schedule.getId()).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_SCHEDULE_ID_NOT_FOUND));
        exist.setStatus(schedule.getStatus());
        if (exist.getStatus().equals(SchedulesStatus.PENDING)) {
            throw new BusinessException(ErrorConstants.ERROR_SCHEDULE_NOT_PENDING);
        }
    }

    @Override
    public Page<Schedule> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public Schedule findOne(String id) {
        log.debug("Request to get Schedule : {}", id);

        Schedule exist = scheduleRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_SCHEDULE_ID_NOT_FOUND));

        return scheduleRepository.findOne(exist.getId());
    }

}
