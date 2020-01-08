package br.com.homedical.service;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.PlaceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by deividi on 31/08/17.
 */
@SuppressWarnings("ALL")
@Service
public class GeocoderUtils {

    private static GeocoderUtils INSTANCE;

    public GeocoderUtils() {
        INSTANCE = this;
    }

    public static GeocoderUtils getInstance() {
        return INSTANCE;
    }

    private static Logger log = LoggerFactory.getLogger(GeocoderUtils.class);

    public static DirectionsResult awaitSilently(DirectionsApiRequest request) {
        try {
            return request.await();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Google API unavailable for request", e);
            throw new IllegalStateException("Google API Unavailable", e);
        }
    }

    public static DistanceMatrix awaitSilently(DistanceMatrixApiRequest request) {
        try {
            return request.await();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Google API unavailable for request", e);
            return null;
        }
    }

    public static PlaceDetails awaitSilently(PlaceDetailsRequest request) {
        try {
            return request.await();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Google API unavailable for request", e);
            throw new IllegalStateException("Google API Unavailable", e);
        }
    }

}
