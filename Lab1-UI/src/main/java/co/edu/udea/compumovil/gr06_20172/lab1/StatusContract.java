package co.edu.udea.compumovil.gr06_20172.lab1;

import android.provider.BaseColumns;

/**
 * Created by Viviana Londoño on 22/08/2017.
 */
public class StatusContract {

    public static final String DB_NAME = "lab1.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER= "usuario";
    public static final String TABLE_LOGIN="logeado";

    public class Column_Login {
        public static final String ID = BaseColumns._ID;
        public static final String EMAIL = "email";
    }
    public class Column_User {
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String LASTNAME = "lastname";
        public static final String GENDER = "gender";
        public static final String DATE = "date";
        public static final String ADDRESS = "address";
        public static final String MAIL = "email";
        public static final String PASS = "pass";
        public static final String CITY = "city";
        public static final String PHONE = "phone";
        public static final String PICTURE = "picture";
    }

}
