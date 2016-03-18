package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.RemoteException;

/**<h1>The manager class for No Data Agent</h1>
 * <p>
 * No Data Agent is an Edge Analytics agent which is only able to receive data via Queries ({@link Query})
 * but is not able to define or send data to Streams ({@link Stream}). In order to define new streams and send
 * data to them, please use {@link DataAgentManager}. </p>
 * <p>
 * No Data Agent can however subscribe to Android System sensors provided by {@link WSO2SystemStreams} and define their
 * own streams that may comprise of any sensor data as necessary (See {@link #subscribeStreamToData(Stream, int...)}).
 * </p>
 * <p>
 *     Alternatively the No Data Agent can add queries ({@link #addQuery(String, String)}) to an existing application
 *     which has its own streams and/or add callbacks ({@link #addCallback(String, String, EdgeAnalyticsCallback)}) in order to
 *     listen to the query events
 * </p>
 * This type of agent may listen to events
 * @author janitha@wso2.org
 */
public class NoDataAgentManager extends EdgeAnalyticsServiceManager {
    public NoDataAgentManager(Context context) {
        super(context);
    }

/*    @Override
    public void addStreamCallback(String stream, String receiver, String provider) throws RemoteException {
        super.addStreamCallback(stream, receiver, provider);
    }

    public void addStreamCallback(String stream, String provider, BroadcastReceiver receiver) throws RemoteException {
        super.addStreamCallback(stream, provider, receiver, context.getPackageName());
    }*/

    /**
     * Receive callbacks to an {@link EdgeAnalyticsCallback} which is registered on the Android Manifest file in a
     * <a href="http://developer.android.com/guide/topics/manifest/receiver-element.html">receiver</a> tag.
     * Create a new class which extends {@link EdgeAnalyticsCallback} and Override its <b>callback</b> method.
     * Be sure to use an intent-filter tag which encloses an action tag in which the android:name attribute is what is to be passed as the receiver.
     * <ul>
     *     <li>The advantage of this over dynamically registered {@link EdgeAnalyticsCallback} is that these are
     *     called even when the recipient application is Destroyed</li>
     *     <li>If the query was defined by the same No Data Agent, use {@link #addCallback(String, String)}</li>
     * </ul>
     *
     * @param streamName the name of the query which is to be listened to
     * @param receiver the android:name attribute of the action tag of the intent-filter of the receiver
     * */
    public void addCallback(String streamName, String receiver, String provider) throws RemoteException {
        super.addStreamCallback(streamName, receiver, provider);
    }

    /**
     * Receive callbacks to a dynamically registered {@link EdgeAnalyticsCallback} which can be used to handle the
     * events of the query with the query name defined by the provider app. Override the callback method in the {@link EdgeAnalyticsCallback}
     * in order to handle the query event.
     *
     * <ul>
     *     <li>A dynamically registered {@link EdgeAnalyticsCallback} does not receive events when the application
     *     is Destroyed, in order to achieve this, use {@link #addCallback(String, String, String)}</li>
     *     <li>If the query was defined by the same No Data Agent, use {@link #addCallback(String, EdgeAnalyticsCallback)}</li>
     * </ul>
     *
     * @param streamName the name of the query which was defined by the provider app
     * @param provider the package name of the provider of the query
     * @param callback an instance {@link EdgeAnalyticsCallback} object where the callback method has been overridden
     *                 in order to handle the event
     * */
    public void addCallback(String streamName, String provider, EdgeAnalyticsCallback callback) throws RemoteException {
        super.addStreamCallback(streamName, provider, callback, context.getPackageName());
    }

    /**
     * Receive callbacks to a dynamically registered {@link EdgeAnalyticsCallback} which can be used to handle the
     * events of the query with the query name defined by the provider app. Override the callback method in the {@link EdgeAnalyticsCallback}
     * in order to handle the query event.
     *
     * <ul>
     *     <li>A dynamically registered {@link EdgeAnalyticsCallback} does not receive events when the application
     *     is Destroyed, in order to achieve this, use {@link #addCallback(String, String, String)}</li>
     *     <li>This is used to listen to queries which were defined by the same No Data Agent if the query was defined
     *     on a different app, use {@link #addCallback(String, String, String)}</li>
     * </ul>
     *
     * @param streamName the name of the query which was defined by the same No Data Agent
     * @param receiver an instance {@link EdgeAnalyticsCallback} object where the callback method has been overridden
     *                 in order to handle the event
     * */
    public void addCallback(String streamName, String receiver) throws RemoteException {
        super.addStreamCallback(streamName, receiver, context.getPackageName());
    }

    /**
     * Receive callbacks to a dynamically registered {@link EdgeAnalyticsCallback} which can be used to handle the
     * events of the query with the query name defined by the provider app. Override the callback method in the {@link EdgeAnalyticsCallback}
     * in order to handle the query event.
     *
     * <ul>
     *     <li>A dynamically registered {@link EdgeAnalyticsCallback} does not receive events when the application
     *     is Destroyed, in order to achieve this, use {@link #addCallback(String, String, String)}</li>
     *     <li>This is used to listen to queries which were defined by the same No Data Agent if the query was defined
     *     on a different app, use {@link #addCallback(String, String, EdgeAnalyticsCallback)}</li>
     * </ul>
     *
     * @param streamName the name of the query which was defined by the same No Data Agent
     * @param callback the android:name attribute of the action tag of the intent-filter of the receiver
     * */
    public void addCallback(String streamName, EdgeAnalyticsCallback callback) throws RemoteException {
        super.addStreamCallback(streamName, context.getPackageName(), callback, context.getPackageName());
    }

    /**
     * Add a {@link Query} by passing the definition as a String in Siddhi QL and the provider of that {@link Stream}
     * (The package name of the app that is the owner of that stream)
     *
     * @param queryDefinition the definition of the stream in Siddhi QL
     * @param provider the package name of the app that originally defined the input streams in the query
     * */
    @Override
    public void addQuery(String queryDefinition, String provider) throws RemoteException {
        super.addQuery(queryDefinition, provider);
    }

    /**
     * Add a {@link Query} by passing the definition as a {@link Stream} and the provider of that {@link Stream}
     * (The package name of the app that is the owner of that stream)
     *
     * @param query the definition of the stream made using {@link Stream} factory class
     * @param provider the package name of the app that originally defined the input streams in the query
     * */
    public void addQuery(Query query, String provider) throws RemoteException {
        super.addQuery(query.create(), provider);
    }

    /**
     * Add a {@link Query} by passing the definition as a String in Siddhi QL if the {@link Stream} that the
     * query is adding as its input Stream(s) was(were) subscribed to by the same No Data Agent
     *
     * @param queryDefinition the definition of the stream in Siddhi QL
     * */
    public void addQuery(String queryDefinition) throws RemoteException {
        super.addQuery(queryDefinition, context.getPackageName());
    }

    /**
     * Add a {@link Query} by passing the a {@link Query} object if the {@link Stream} that the
     * query is adding as its input Stream(s) was(were) subscribed to by the same No Data Agent
     *
     * @param query the query defined using the {@link Query} factory class
     * */
    public void addQuery(Query query) throws RemoteException {
        super.addQuery(query.create(), context.getPackageName());
    }

    /**
     * Subscribe a new stream to data from the System Sensors provided by the {@link WSO2SystemStreams}
     *  @param streamDefinition the definition of the stream in Siddhi QL
     *  @param inputTypes the sensors values (Use {@link WSO2SystemStreams}) that should be mapped to the corresponding
     *                    attributes in the same order as the data was defined
     * */
    public void subscribeStreamToData(String streamDefinition, int... inputTypes) throws RemoteException {
        super.subscribeStreamToData(context.getPackageName(), streamDefinition, inputTypes);
    }

    /**
     * Subscribe a new stream to data from the System Sensors provided by the {@link WSO2SystemStreams}
     *  @param stream the stream built using the {@link Stream} factory class
     *  @param inputTypes the sensors values (Use {@link WSO2SystemStreams}) that should be mapped to the corresponding
     *                    attributes in the same order as the data was defined
     * */
    public void subscribeStreamToData(Stream stream, int... inputTypes) throws RemoteException {
        super.subscribeStreamToData(context.getPackageName(), stream.create(), inputTypes);
    }
}
