package com.mapnote.mapnoteserver.domain.schedule.util;

import com.mapnote.mapnoteserver.domain.schedule.vo.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeometryUtil {

  private static final WKTReader wktReader = new WKTReader();

  public static Coordinate calculate(Double baseLatitude, Double baseLongitude, Double distance, Double bearing) {

    double radianLatitude = toRadian(baseLatitude);
    double radianLongitude = toRadian(baseLongitude);
    double radianAngle = toRadian(bearing);
    double distanceRadius = distance / 6371.01;

    double latitude = Math.asin(sin(radianLatitude) * cos(distanceRadius) +
        cos(radianLatitude) * sin(distanceRadius) * cos(radianAngle));
    double longitude = radianLongitude + Math.atan2(sin(radianAngle) * sin(distanceRadius) *
        cos(radianLatitude), cos(distanceRadius) - sin(radianLatitude) * sin(latitude));

    longitude = normalizeLongitude(longitude);

    return new Coordinate(toDegree(latitude), toDegree(longitude));
  }

  public static Geometry wktToGeometry(String text) {
    Geometry geometry = null;
    try {
      geometry = wktReader.read(text);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return geometry;
  }

  private static double sin(Double rad) {
    return Math.sin(rad);
  }

  private static double cos(Double rad) {
    return Math.cos(rad);
  }

  private static double toRadian(double deg) {
    return deg * Math.PI / 180.0;
  }

  private static double toDegree(double rad) {
    return rad * 180.0 / Math.PI;
  }

  private static double normalizeLongitude(double longitude) {
    return (longitude + 540) % 360 - 180;
  }

}
