package br.edu.ifsp.dmo.ifitness.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Badges implements Serializable {

    @NonNull
    @PrimaryKey
    private String id;
    private String physicalActivitiesId;
    private String relativeDistance;
    private String badge;


    public Badges(String user, String physicalActivitiesId, String badge) {
        this.id = UUID.randomUUID().toString();
        this.physicalActivitiesId = physicalActivitiesId;
        this.badge = badge;
    }

    @Ignore
    public Badges() {
        this("", "", "");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPhysicalActivitiesId() {
        return physicalActivitiesId;
    }

    public void setPhysicalActivitiesId(String physicalActivitiesId) {
        this.physicalActivitiesId = physicalActivitiesId;
    }

    public String getRelativeDistance() {
        return relativeDistance;
    }

    public void setRelativeDistance(String relativeDistance) {
        this.relativeDistance = relativeDistance;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Badges)) return false;
        Badges badges = (Badges) o;
        return id.equals(badges.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
