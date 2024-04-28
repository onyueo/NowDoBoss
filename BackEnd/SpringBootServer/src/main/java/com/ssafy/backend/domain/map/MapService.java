package com.ssafy.backend.domain.map;

import java.util.List;
import java.util.Map;

public interface MapService {
    Map<String, List<List<Double>>> getCommercialAreaCoords(double ax, double ay, double bx, double by) throws Exception;
    Map<String, List<List<Double>>> getAdministrationAreaCoords(double ax, double ay, double bx, double by) throws Exception;
    Map<String, List<List<Double>>> getDistrictAreaCoords(double ax, double ay, double bx, double by) throws Exception;

}
