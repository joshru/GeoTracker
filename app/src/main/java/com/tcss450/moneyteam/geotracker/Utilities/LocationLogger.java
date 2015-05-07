package com.tcss450.moneyteam.geotracker.Utilities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/1/2015.
 */
public class LocationLogger implements Parcelable {
    private List<Location> mLocationList;

    public LocationLogger() {
        mLocationList = new ArrayList<>();
    }

    public LocationLogger(final Parcel input) {
        mLocationList = new ArrayList<>();
        mLocationList = input.readArrayList(Location.class.getClassLoader());
    }

    public void addLocation(final Location location) {
        mLocationList.add(location);
    }

    public List<Location> getLocationList() {
        return mLocationList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel output, final int flags) {
        output.writeList(mLocationList);
    }

    public static final Parcelable.Creator<LocationLogger> CREATOR
            = new Parcelable.Creator<LocationLogger>() {

        public LocationLogger createFromParcel(final Parcel input) {
            return new LocationLogger(input);
        }

        public LocationLogger[] newArray(final int size) {
            return new LocationLogger[size];
        }
    };
}
