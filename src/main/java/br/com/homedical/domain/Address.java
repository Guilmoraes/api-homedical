package br.com.homedical.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Address extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "district")
    private String district;

    @Column(name = "complement")
    private String complement;

    @ManyToOne
    private City city;

    private Double lat;

    private Double lng;

    @JsonIgnore
    @Column(name = "valid_geo_address")
    private Boolean isValidAddress;


    public String buildFullAddress() {
        StringBuilder sangueBao = new StringBuilder();

        if (StringUtils.isNotBlank(street)) {
            sangueBao.append(street);
        }

        if (StringUtils.isNotBlank(number)) {
            sangueBao.append(" ");
            sangueBao.append(number);
        }

        if (StringUtils.isNotBlank(zipcode)) {
            sangueBao.append(" ");
            sangueBao.append(zipcode);
        }

        if (city != null) {
            if (StringUtils.isNotBlank(city.getName())) {
                sangueBao.append(" ");
                sangueBao.append(city.getName());
            }

            if (city.getState() != null && StringUtils.isNotBlank(city.getState().getName())) {
                sangueBao.append(" ");
                sangueBao.append(city.getState().getName());
            }

        }

        return sangueBao.toString();
    }
}
