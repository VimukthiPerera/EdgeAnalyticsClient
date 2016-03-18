package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 12/13/15.
 */
public class EdgeAnalyticsServiceManager extends WSO2ServiceManager {

    public boolean service_connected = false;
    protected Context context;
    protected IEdgeAnalyticsService edgeAnalyticsService;
    protected List<Stream> streams = new ArrayList<>();
    protected List<Query> queries = new ArrayList<>();
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            edgeAnalyticsService = IEdgeAnalyticsService.Stub.asInterface(service);
            service_connected = true;
            PostponedMethodStack.doMethods(broadcastReceiverMap, context, edgeAnalyticsService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service_connected = false;
            edgeAnalyticsService = null;
        }
    };
    private Map<String, BroadcastReceiver> broadcastReceiverMap = new Hashtable<>();

    public EdgeAnalyticsServiceManager(Context context) {
        this.context = context;
        bindToWSO2Service();
    }

    @Override
    protected void bindToWSO2Service() {
        Intent intent = new Intent();
        intent.putExtra("package", context.getPackageName());
        intent.setClassName("org.wso2.edgeanalyticsservice", "org.wso2.edgeanalyticsservice.EdgeAnalyticsService");
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void unbindFromWSO2Service() {
        for (BroadcastReceiver r : broadcastReceiverMap.values()) {
            context.unregisterReceiver(r);
        }
        try {
            stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        context.unbindService(connection);
    }

    //TODO implement auto recalling of methods when !service_connected

    private void stop() throws RemoteException {
        if(service_connected) {
            edgeAnalyticsService.stop();
        } else {
            PostponedMethodStack.addMethod("stop", edgeAnalyticsService);
        }
    }

    //TODO fix start() to reInitialize() issue

    protected void addStream(String streamDefinition, String packageName) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.addStream(streamDefinition, packageName);
            start(packageName);
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addStreamMethod(streamDefinition, packageName));
        }
    }

    protected void addQuery(String queryDefinition, String packageName) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.addQuery(queryDefinition, packageName);
            start(packageName);
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addQueryMethod(queryDefinition, packageName));
        }
    }

    protected void addStreamCallback(String stream, String receiver, String packageName) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.addStreamCallback(stream, receiver, packageName, context.getPackageName());
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addStreamCallbackMethod(stream, receiver, packageName));
        }
    }

    protected void addStreamCallback(String stream, String packageName, BroadcastReceiver receiver, String reveiverPkg) throws RemoteException {
        if (service_connected) {
            broadcastReceiverMap.put(stream, receiver);
            IntentFilter intentFilter = new IntentFilter(stream);
            context.registerReceiver(receiver, intentFilter);
            edgeAnalyticsService.addDynamicStreamCallback(stream, packageName, reveiverPkg);
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addStreamCallbackMethod(stream, packageName, receiver, reveiverPkg));
        }
    }

    protected void addQueryCallback(String queryName, String receiver, String packageName) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.addQueryCallback(queryName, receiver, packageName, context.getPackageName());
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addQueryCallbackMethod(queryName, receiver, packageName));
        }
    }

    protected void addQueryCallback(String queryName, String packageName, BroadcastReceiver receiver, String reveiverPkg) throws RemoteException {
        if (service_connected) {
            broadcastReceiverMap.put(queryName, receiver);
            IntentFilter intentFilter = new IntentFilter(queryName);
            context.registerReceiver(receiver, intentFilter);
            edgeAnalyticsService.addDynamicQueryCallback(queryName, packageName, reveiverPkg);
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.addQueryCallbackMethod(queryName, packageName, receiver, reveiverPkg));
        }
    }

    protected void start(String packageName) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.startExecutionPlan(packageName);
        } else {
            PostponedMethodStack.addMethod("start", edgeAnalyticsService, packageName);
        }
    }

    protected void validateExecutionPlan() throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.validateExecutionPlan(context.getPackageName());
        }
    }

    protected void sendDataToService(String stream, Object data[], String packageName) throws RemoteException {
        if (service_connected) {
            List<String> values = new ArrayList<>(data.length);
            List<String> types = new ArrayList<>(data.length);
            for (Object o : data) {
                if (o instanceof Integer) {
                    values.add(o+"");
                    types.add("int");
                } else if (o instanceof String) {
                    values.add(o+"");
                    types.add("string");
                } else if (o instanceof Double) {
                    values.add(o+"");
                    types.add("double");
                } else if (o instanceof Float) {
                    values.add(o+"");
                    types.add("float");
                }
            }
            edgeAnalyticsService.sendData(packageName, stream, values, types);
        }
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void unregisterAllReceivers() {
        for (BroadcastReceiver r : broadcastReceiverMap.values()) {
            context.unregisterReceiver(r);
        }
    }

    protected void subscribeStreamToData(String packageName, String streamDefinition, int... inputTypes) throws RemoteException {
        if (service_connected) {
            edgeAnalyticsService.subscribeStreamToData(packageName, streamDefinition, inputTypes);
            edgeAnalyticsService.subscribeExecutionPlan(packageName);
        } else {
            PostponedMethodStack.addMethod(PostponedMethod.subscribeStreamToDataMethod(packageName, streamDefinition, inputTypes));
        }
    }

    public List<Stream> getAllStreams() throws RemoteException {
        return edgeAnalyticsService.getAllStreams();
    }
}
