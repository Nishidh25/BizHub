package com.bd.bizhub.model;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;

import io.realm.RealmObject;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass(embedded = true)
public class Project extends RealmObject {
    @Nullable
    private String name;
    @Nullable
    private String partition;
   // private ObjectId id;
    public Project(String name,String partition){
        this.name = name;
        this.partition = partition;
      //  this.id = new ObjectId();
    }
    // Standard getters & setters
    public Project(){}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPartition() { return partition; }
    public void setPartition(String partition) { this.partition = partition; }
}