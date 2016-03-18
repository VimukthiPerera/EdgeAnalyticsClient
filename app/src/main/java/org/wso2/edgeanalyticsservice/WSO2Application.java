package org.wso2.edgeanalyticsservice;

import android.app.Application;
import android.util.Log;

import java.util.Hashtable;
import java.util.Map;

//import android.content.Context;

/**
 * <h1>The base application on which all WSO2 services run</h1>
 *
 * Initialize an instance and use {@link #getWSO2Service(String)} to get an instance of a service manager
 */
public class WSO2Application extends Application {

    private static WSO2Application singleton;
    private static Map<String, WSO2ServiceManager> wso2ServiceManager = new Hashtable<>();

    public static WSO2Application getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    //TODO figure out where to unbind from service

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (WSO2ServiceManager e : wso2ServiceManager.values()) {
            try {
                e.unbindFromWSO2Service();
            } catch (Exception e1) {
                Log.e("WSO2Application", "No Service "+e.toString());
            }
        }
    }

    /**
     * Get an instance of a service manager by passing in the type of service. It is the developers responsibility
     * to stop the service using {@link #stopWSO2Service(String)} to optimize resource usage.
     *
     * @param wso2Context use {@link WSO2Context} to get the type of service manager that is to be used
     * */
    public Object getWSO2Service(String wso2Context) {
        switch (wso2Context) {
            case "Data":
                wso2ServiceManager.put(wso2Context, new DataAgentManager(this));
                return wso2ServiceManager.get(wso2Context);
            /*case "Data":
                wso2ServiceManager.put(wso2Context, new DataOnlyAgentManager(this));
                return wso2ServiceManager.get(wso2Context);*/
            case "NoData":
                wso2ServiceManager.put(wso2Context, new NoDataAgentManager(this));
                return wso2ServiceManager.get(wso2Context);
            default:
                throw new UnsupportedOperationException("The requested service does not exist");
        }
    }

    /**
     * Unbinds the current application from the indicated service. Does not guarantee that the service is stopped.
     *
     * @param wso2Context the service to unbind from, use {@link WSO2Context} to get the String name of the service
     * @return boolean : whether the service was successfully unbound
     * */
    public boolean stopWSO2Service(String wso2Context) {
        try {
            WSO2ServiceManager e = wso2ServiceManager.get(wso2Context);
            e.unbindFromWSO2Service();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
