package com.scbpfsdgis.femobilebetav20.data.model;

/**
 * Created by William on 1/7/2018.
 */

public class Person {

    public static final String TAG = Person.class.getSimpleName();
    public static final String TABLE = "person";

    //Column Names
    public static final String COL_PRSN_ID = "person_id";
    public static final String COL_PRSN_NAME = "person_name";
    public static final String COL_PRSN_CLS = "person_class";
    public static final String COL_PRSN_CONT = "person_contact";

    private int personID;
    private String personName;
    private String personType;
    private String personCont;

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonCls() {
        return personType;
    }

    public void setPersonCls(String personType) {
        this.personType = personType;
    }

    public String getPersonCont() {
        return personCont;
    }

    public void setPersonCont(String personCont) {
        this.personCont = personCont;
    }
}
