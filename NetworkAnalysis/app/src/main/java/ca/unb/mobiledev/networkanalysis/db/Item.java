package ca.unb.mobiledev.networkanalysis.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlin.internal.InlineOnly;

@Entity(tableName = "oui")  // Represents a SQLite table
public class Item {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String Registry;
    @NonNull
    private String Assignment;
    @NonNull
    private String OrganizationName;
    private String OrganizationAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistry() {
        return Registry;
    }

    public void setRegistry(String registry) {
        Registry = registry;
    }

    public String getAssignment() {
        return Assignment;
    }

    public void setAssignment(String assignment) {
        Assignment = assignment;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getOrganizationAddress() {
        return OrganizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        OrganizationAddress = organizationAddress;
    }
}