package models;


/**
 * Created by ngadre on 10/28/15.
 */


import utils.CouchDB;

public class CouchModel {

    public void save(){
        CouchDB.saveDocument(this);
    }

    public static Object findByIdAndObjectClass(String id,Class klass) {
        return CouchDB.find(klass,id);
    }
}
