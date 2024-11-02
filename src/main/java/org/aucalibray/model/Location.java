package org.aucalibray.model;

import org.aucalibray.aucaenum.LocationType;
import java.util.UUID;

public class Location {
    private String locationCode;
    private UUID locationId;
    private String locationName;
    private LocationType locationType;
    private UUID parentId;

    public Location() {
    }

    public Location(String locationCode, String locationName, LocationType locationType, UUID parentId) {
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.locationType = locationType;
        this.parentId = parentId;
        this.locationId = UUID.randomUUID();
    }

    public Location(UUID locationId,String locationCode, String locationName, LocationType locationType, UUID parentId) {
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.locationType = locationType;
        this.parentId = parentId;
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
