package com.bd.bizhub.model;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    @Required
    private String _id;
    @Required
    private String _partition;
    private RealmList<Project> memberOf;
    @Required
    private String name;
    // Standard getters & setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public String get_partition() { return _partition; }
    public void set_partition(String _partition) { this._partition = _partition; }
    public RealmList<Project> getMemberOf() { return memberOf; }
    public void setMemberOf(RealmList<Project> memberOf) { this.memberOf = memberOf; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}