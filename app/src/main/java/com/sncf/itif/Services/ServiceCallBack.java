package com.sncf.itif.Services;

/**
 * Created by Rahghul on 06/02/2016.
 */
public interface ServiceCallBack {
    void serviceSuccess(Object object, int id_srv);

    void serviceFailure(Exception exception);

}
