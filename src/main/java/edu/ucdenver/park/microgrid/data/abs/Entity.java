/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package edu.ucdenver.park.microgrid.data.abs;

import java.io.*;
import java.util.Objects;

/**
 * Entity
 *
 * immutable
 *
 * All data objects for this project will have unique _ids. Objects should always be compared using these ids.
 *  I do not specify how to create this unique id or enforce its uniquness. However, the system will break if it
 *  is not unique for each object.
 *
 *  I recommend using UUID4 for this.
 *
 * Example subclasses: Graph, Node, Edge, Datum
 *
 * @author Jake Billings
 */
public abstract class Entity implements Externalizable {
    /**
     * _id
     *
     * string
     *
     * unique string identifier for this entity; must be globally unique; uuid_4 is a good option
     */
    private String _id;

    /**
     * Entity
     *
     * constructor: empty
     *
     * for use with deserialization; do not use for instantiation
     */
    protected Entity() {

    }

    /**
     * Entity()
     *
     * superconstructor for all entities; requires a uniuqe _id param
     *
     * @param _id a uniuqe string _id for this entity
     */
    public Entity(String _id) {
        if (_id == null) throw new IllegalArgumentException("_id must not be null when instantiation an entity");
        if (_id.equals("")) throw new IllegalArgumentException("_id must not be empty string when instantiation an entity");
        this._id = _id;
    }

    /**
     * equals()
     *
     * I override .equals() to compare _ids for each object. Again, this is why _id MUST be globally unqiue.
     *
     * @param o the other entity to compare against "this"
     * @return true if _id from this matches _id from o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(_id, entity._id);
    }

    /**
     * hashCode()
     *
     * we trust that the _id is unique, so hash only the _id for the object's hash code
     *
     * @return Objects.hash(_id)
     */
    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    //----Getters----
    public String get_id() {
        return _id;
    }

    //----Setters----
    protected void set_id(String _id) {
        this._id = _id;
    }

    //----Externalizers----
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.get_id());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.set_id(in.readUTF());
    }
}
