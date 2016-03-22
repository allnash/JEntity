package models;


/**
 * Created by ngadre on 10/28/15.
 */


import utils.DGCouchDB;

public class CouchModel {

    public void save(){
        DGCouchDB.saveDocument(this);
    }

    public static Object findByIdAndObjectClass(String id,Class klass) {
        return DGCouchDB.find(klass,id);
    }
}
