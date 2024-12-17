package com.sba.processingunit.api;

public class UrlMappings {
    private static class Common {
        public static final String CREATE = "/create";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete";
        public static final String GET = "/get";
        public static final String GET_ALL = "/all";
    }

    static class User {
        public static final String ROOT = "user";
        public static final String CREATE_USER = Common.CREATE;
        public static final String UPDATE_USER = Common.UPDATE;
        public static final String DELETE_USER = Common.DELETE;
        public static final String GET_USER = Common.GET;
        public static final String GET_ALL_USERS = Common.GET_ALL;
    }
}
