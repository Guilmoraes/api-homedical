package br.com.homedical.service;

import br.com.homedical.domain.Address;
import br.com.homedical.repository.AddressRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceDetails;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeocoderService {

    private final Logger log = LoggerFactory.getLogger(GeocoderService.class);

    public static final int SRID = 4326;
    public static PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.maximumPreciseValue);
    public static GeometryFactory geometryFactory = new GeometryFactory(precisionModel, SRID);

    private final AddressRepository addressRepository;

    private final GeoApiContext context;

    private final ComponentFilter BR_COMPONENT_FILTER = ComponentFilter.country("BR");

    public GeocoderService(GeoApiContext context, AddressRepository addressRepository) {
        this.context = context;
        this.addressRepository = addressRepository;
    }

    private void checkAddressSync(Address address) {
        log.info("checkAddressSync {}", address);
        processAddress(address);

        Address one = addressRepository.findOne(address.getId());

        addressRepository.save(one);
    }

    private void processAddress(Address address) {

        log.info("processAddress {}", address);

        if (address != null && address.getIsValidAddress() == null) {
            Point point = geometryFactory.createPoint(new Coordinate(address.getLng(), address.getLat()));
            point = getPointAddress(address, point);
            if (point == null) {
                address.setIsValidAddress(Boolean.FALSE);
            } else {
                address.setIsValidAddress(Boolean.TRUE);
                address.setLat(point.getY());
                address.setLng(point.getX());
            }

        }
    }

    @Scheduled(cron = "0 0 */12 * * *", zone = "America/Sao_Paulo")
    public void processPending() {

        log.info("Processing all pending addresses");

        List<Address> allWithoutAddressPoint = addressRepository.findByLatIsNullAndLngIsNullAndIsValidAddressIsNull();

        log.info("Processing all pending addresses for customers. Size: {}", allWithoutAddressPoint.size());

        if (!allWithoutAddressPoint.isEmpty()) {
            allWithoutAddressPoint.forEach(this::checkAddressSync);
        }
    }

    private Point getPointAddress(Address address, Point point) {
        try {
            String fullAddress = address.buildFullAddress();

            try {
                GeocodingResult[] results = GeocodingApi.geocode(context, fullAddress).components(BR_COMPONENT_FILTER).await();
                if (results.length > 0) {
                    GeocodingResult result = results[0];
                    if (result.geometry != null && result.geometry.location != null) {
                        LatLng location = result.geometry.location;
                        point = geometryFactory.createPoint(new Coordinate(location.lng, location.lat));
                    }
                } else {
                    fullAddress = address.getCity().getName() + " - " + address.getCity().getState().getAcronym();
                    results = GeocodingApi.geocode(context, fullAddress).components(BR_COMPONENT_FILTER).await();
                    if (results.length > 0) {
                        GeocodingResult result = results[0];
                        if (result.geometry != null && result.geometry.location != null) {
                            LatLng location = result.geometry.location;
                            point = geometryFactory.createPoint(new Coordinate(location.lng, location.lat));
                        }
                    }
                }
            } catch (ApiException | InterruptedException e) {
                log.error("Error getting address point for {}", address, e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("Error getting address from geocoder", e);
        }
        return point;
    }

    /**
     * Usa as predictions da api <b>placeAutocomplete</b> para obter os detalhes dos endereços da api <b>placeDetails</b>
     *  @param address
     * @param latitude
     * @param longitude @return
     * @param origin
     */
    public List<PlaceDetails> searchAddress(String address, Double latitude, Double longitude, Boolean origin) {
        AutocompletePrediction[] predictions = placeAutocompleteSearch(address);

        if (predictions != null) {
            List<PlaceDetails> result = new ArrayList<>();

            for (AutocompletePrediction p : predictions) {
                result.add(findByPlaceId(p.placeId));
            }

            return result;
        }

        return null;
    }

    private AutocompletePrediction[] placeAutocompleteSearch(String address) {
        try {
            return PlacesApi.placeAutocomplete(context, address).components(BR_COMPONENT_FILTER).language("PT-BR").await();
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Error getting place autocomplete {}", address, e);
        }
        return null;
    }

    public PlaceDetails findByPlaceId(String placeId) {
        PlaceDetailsRequest language = PlacesApi.placeDetails(context, placeId).language("PT-BR");
        return GeocoderUtils.awaitSilently(language);
    }

    private AutocompletePrediction[] placeAutocompleteSearch(String address, Double latitude, Double longitude, Boolean origin) {
        try {
            AutocompletePrediction[] await = PlacesApi.placeAutocomplete(context, address).types(PlaceAutocompleteType.ADDRESS).components(BR_COMPONENT_FILTER).language("PT-BR").await();
            AutocompletePrediction[] await2;
            if (latitude != null && longitude != null) {
                await2 = PlacesApi.queryAutocomplete(context, address).language("PT-BR").radius(origin != null && origin.equals(Boolean.TRUE) ? 100000 : 300000).location(new LatLng(latitude, longitude)).await();
            } else {
                await2 = PlacesApi.placeAutocomplete(context, address).components(BR_COMPONENT_FILTER).language("PT-BR").await();
            }
            return ArrayUtils.addAll(await2, ArrayUtils.addAll(await));
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Error getting place autocomplete {}", address, e);
        }
        return null;
    }


}

