package br.com.homedical.config;

import br.com.homedical.service.mapper.AddressGeocodingMapper;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by deividi on 22/08/17.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
public class MapsApiConfiguration {

    private final ApplicationProperties properties;

    @Autowired
    public MapsApiConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
            .apiKey(properties.getMapKey())
            .maxRetries(5)
            .build();
    }

    @Bean
    public AddressGeocodingMapper getAddressGeocoderMapper() {
        return new AddressGeocodingMapper();
    }
}
