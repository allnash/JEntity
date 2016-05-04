package xyz.gadre.jentity.models;

import com.google.common.base.CaseFormat;

/**
 * Created by ngadre on 5/2/16.
 */
public abstract class DocumentFactoryModel {

    public static String jsonKeyNameForClass(Class c){
        String className = c.getSimpleName();
        className = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        return className;
    }

    public static Owner isValidOwnerId(String ownerId){
        Owner requestOwner = Owner.findById(ownerId);
        if(requestOwner == null){
            requestOwner = Owner.findByExternalId(ownerId);
        }
        return requestOwner;
    }
}
