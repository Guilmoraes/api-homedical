package br.com.homedical.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "professional")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude = {"specialties", "duties", "patients"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Professional extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "professional_specialties",
        joinColumns = @JoinColumn(name = "professional_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Specialty> specialties = new HashSet<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @NotNull
    private Phone phone;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @NotNull
    private Address address;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Valid
    @ManyToMany
    @JoinTable(name = "professional_patients",
        joinColumns = @JoinColumn(name = "professional_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"))
    @Builder.Default
    @JsonIgnore
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Builder.Default
    @JoinTable(name = "professional_duty",
        joinColumns = @JoinColumn(name = "duties_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "professional_id", referencedColumnName = "id"))
    private Set<Duty> duties = new HashSet<>();
}

