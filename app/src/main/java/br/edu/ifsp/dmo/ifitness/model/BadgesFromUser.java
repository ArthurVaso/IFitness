package br.edu.ifsp.dmo.ifitness.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BadgesFromUser {

    @Embedded
    private Badges badges;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    private List<PhysicalActivities> physicalActivities;
/*
    public UserWithActivities() {
        physicalActivities = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PhysicalActivities> getPhysicalActivities() {
        return physicalActivities;
    }

    public void setPhysicalActivities(List<PhysicalActivities> physicalActivities) {
        this.physicalActivities = physicalActivities;
    }
     */
}
