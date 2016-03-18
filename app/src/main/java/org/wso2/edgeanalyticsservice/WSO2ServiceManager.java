package org.wso2.edgeanalyticsservice;

/**
 * Created by root on 12/13/15.
 */
public abstract class WSO2ServiceManager {

    protected abstract void bindToWSO2Service();

    protected abstract void unbindFromWSO2Service();

}