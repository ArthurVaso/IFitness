package br.edu.ifsp.dmo.ifitness.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

@Entity(tableName = "physical-activities")
public class PhysicalActivities implements Serializable {

    @PrimaryKey
    @Nonnull
    private String id;
    private String user;
    private String activityCategory;
    private String distance;
    private String duration;
    private String activityDate;
    private String timestamp;

    public PhysicalActivities(String user,
                              String activityCategory,
                              String distance, String duration,
                              String activityDate,
                              String timestamp) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.activityCategory = activityCategory;
        this.distance = distance;
        this.duration = duration;
        this.activityDate = activityDate;
        this.timestamp = timestamp;
    }

    @Ignore
    public PhysicalActivities() {
        this("", "",
                "", "", "",
                "");
    }

    @Nonnull
    public String getId() {
        return id;
    }

    public void setId(@Nonnull String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalActivities)) return false;
        PhysicalActivities that = (PhysicalActivities) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
