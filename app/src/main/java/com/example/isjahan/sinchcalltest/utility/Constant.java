package com.example.isjahan.sinchcalltest.utility;

/**
 * Created by shadman.rahman on 05-Jun-17.
 */




public class Constant {
    public static class Database{
        public static final String DATABASE_NAME = "mars-e7077";
        public static final int DATABASE_VERSION = 1;
        public static final String TABLE_USER = "callusers";
        public static final String TABLE_CHAT_ROOMS = "chat_rooms";

        public static class User{
            public static final String UID = "uid";
            public static final String IS_ME = "isMe";
        }

        public static class CallLog{
            public static String CALLBY = "uid";
            public static String CALLTO = "rid";
            public static String CALLDURATION = "duration";
            public static String INITIATEDAT = "initiatedat";
            public static String CALLTYPE = "calltype";

        }



        public static class Group{
            public static final String GROUP_ID = "groupId";
            public static final String OWNER = "owner";
            public static final String ADMIN = "admin";
            public static final String NAME = "name";
            public static final String MEMBER = "member";
        }


    }

    public static class Storage{
        public static final String STORAGE_URL = "gs://mars-e7047.appspot.com";
        public static final String ATTACHMENT = "attachments";
    }
}

