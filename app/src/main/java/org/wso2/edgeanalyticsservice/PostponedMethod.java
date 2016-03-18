package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 1/12/16.
 */
public class PostponedMethod {
    private String methodName;
    private List<Object> args = new ArrayList<>();
    private IEdgeAnalyticsService edgeAnalyticsService;

    public PostponedMethod(String methodName, Object... args) {
        this.methodName = methodName;
        this.args.addAll(Arrays.asList(args));
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setEdgeAnalyticsService(IEdgeAnalyticsService edgeAnalyticsService) {
        this.edgeAnalyticsService = edgeAnalyticsService;
    }

    public static PostponedMethod addStreamMethod(String streamDefinition, String packageName) {
        PostponedMethod pm = new PostponedMethod("addStream");
        pm.getArgs().add(0,streamDefinition);
        pm.getArgs().add(1,packageName);
        return pm;
    }

    public static PostponedMethod addQueryMethod(String queryDefinition, String packageName) {
        PostponedMethod pm = new PostponedMethod("addQuery");
        pm.getArgs().add(0, queryDefinition);
        pm.getArgs().add(1, packageName);
        return pm;
    }

    public static PostponedMethod addStreamCallbackMethod(String stream, String receiver, String packageName) {
        PostponedMethod pm = new PostponedMethod("addStreamCallback");
        pm.getArgs().add(0,stream);
        pm.getArgs().add(1,receiver);
        pm.getArgs().add(2, packageName);
        return pm;
    }

    public static PostponedMethod addStreamCallbackMethod(String stream, String packageName, BroadcastReceiver receiver, String reveiverPkg){
        PostponedMethod pm = new PostponedMethod("addStreamCallback");
        pm.getArgs().add(0,stream);
        pm.getArgs().add(1,packageName);
        pm.getArgs().add(2,receiver);
        pm.getArgs().add(3,reveiverPkg);
        return pm;
    }

    public static PostponedMethod addQueryCallbackMethod(String queryName, String receiver, String packageName) {
        PostponedMethod pm = new PostponedMethod("addStreamCallback");
        pm.getArgs().add(0,queryName);
        pm.getArgs().add(1,receiver);
        pm.getArgs().add(2,packageName);
        return pm;
    }

    public static PostponedMethod addQueryCallbackMethod(String queryName, String packageName, BroadcastReceiver receiver, String reveiverPkg) throws RemoteException {
        PostponedMethod pm = new PostponedMethod("addQueryCallback");
        pm.getArgs().add(0,queryName);
        pm.getArgs().add(1,packageName);
        pm.getArgs().add(2,receiver);
        pm.getArgs().add(3,reveiverPkg);
        return pm;
    }

    public static PostponedMethod subscribeStreamToDataMethod(String packageName, String streamDefinition, int... inputTypes) throws RemoteException {
        PostponedMethod pm = new PostponedMethod("subscribeStreamToData");
        pm.getArgs().add(0,packageName);
        pm.getArgs().add(1, streamDefinition);
        pm.getArgs().add(2,inputTypes);
        return pm;
    }

    public void doMethod() throws RemoteException {
        edgeAnalyticsService = PostponedMethodStack.edgeAnalyticsService;
        switch (methodName) {
            case "stop":
                stop();
                break;
            case "addStream":
                addStream((String)args.get(0), (String)args.get(1));
                break;
            case "addQuery":
                addQuery((String)args.get(0), (String)args.get(1));
                break;
            case "addStreamCallback":
                if(args.size() == 3) {
                    addStreamCallback((String) args.get(0), (String) args.get(1), (String) args.get(2));
                } else {
                    addStreamCallback((String) args.get(0), (String) args.get(1), (BroadcastReceiver) args.get(2), (String) args.get(3));
                }
                break;
            case "addQueryCallback":
                if(args.size() == 3) {
                    addQueryCallback((String) args.get(0), (String) args.get(1), (String) args.get(2));
                } else {
                    addQueryCallback((String) args.get(0), (String) args.get(1), (BroadcastReceiver) args.get(2), (String) args.get(3));
                }
                break;
            case "start":
                start((String) args.get(0));
                break;
            case "subscribeStreamToData":
                subscribeStreamToData((String) args.get(0), (String) args.get(1), (int []) args.get(2));
        }
    }

    private void stop() throws RemoteException {
        edgeAnalyticsService.stop();
    }

    protected void addStream(String streamDefinition, String packageName) throws RemoteException {
        edgeAnalyticsService.addStream(streamDefinition, packageName);
        start(packageName);
    }

    protected void addQuery(String queryDefinition, String packageName) throws RemoteException {
        edgeAnalyticsService.addQuery(queryDefinition, packageName);
        start(packageName);
    }

    protected void addStreamCallback(String stream, String receiver, String packageName) throws RemoteException {
        edgeAnalyticsService.addStreamCallback(stream, receiver, packageName, PostponedMethodStack.context.getPackageName());
    }

    protected void addStreamCallback(String stream, String packageName, BroadcastReceiver receiver, String reveiverPkg) throws RemoteException {
        PostponedMethodStack.broadcastReceiverMap.put(stream, receiver);
        IntentFilter intentFilter = new IntentFilter(stream);
        PostponedMethodStack.context.registerReceiver(receiver, intentFilter);
        edgeAnalyticsService.addDynamicStreamCallback(stream, packageName, reveiverPkg);
    }

    protected void addQueryCallback(String queryName, String receiver, String packageName) throws RemoteException {
        edgeAnalyticsService.addQueryCallback(queryName, receiver, packageName, PostponedMethodStack.context.getPackageName());
    }

    protected void addQueryCallback(String queryName, String packageName, BroadcastReceiver receiver, String reveiverPkg) throws RemoteException {
        PostponedMethodStack.broadcastReceiverMap.put(queryName, receiver);
        IntentFilter intentFilter = new IntentFilter(queryName);
        PostponedMethodStack.context.registerReceiver(receiver, intentFilter);
        edgeAnalyticsService.addDynamicQueryCallback(queryName, packageName, reveiverPkg);
    }

    protected void start(String packageName) throws RemoteException {
        edgeAnalyticsService.startExecutionPlan(packageName);
    }

    protected void subscribeStreamToData(String packageName, String streamDefinition, int... inputTypes) throws RemoteException {
        edgeAnalyticsService.subscribeStreamToData(packageName, streamDefinition, inputTypes);
        edgeAnalyticsService.subscribeExecutionPlan(packageName);
    }
}
