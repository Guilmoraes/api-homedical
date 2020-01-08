package br.com.homedical.domain;

import br.com.homedical.domain.enumeration.SchedulesStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Schedule extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid-custom")
    @GenericGenerator(name = "uuid-custom", strategy = "br.com.homedical.config.UuidCustomGenerator")
    private String id;

    @Column(name = "start")
    private ZonedDateTime start;

    @Column(name = "finish")
    private ZonedDateTime finish;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SchedulesStatus status;

    @Valid
    @ManyToOne
    private Patient patient;

    @Valid
    @ManyToOne
    private Professional professional;

    @Valid
    @ManyToOne
    private Duty duty;

    @OneToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Signature> signatures = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "schedule_images",
        joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<File> scheduleImages = new HashSet<>();

}
