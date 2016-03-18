package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.RemoteException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**<h1>The manager class for Data Agent</h1>
 * <p>
 * Data Agent is an Edge Analytics agent which is able to send data to Streams ({@link Stream}) and receive data via Queries ({@link Query}).
 * </p>
 * This type of agent may listen to events and report events to Siddhi
 * @author janitha@wso2.org
 */
public class DataAgentManager extends EdgeAnalyticsServiceManager {
    public DataAgentManager(Context context) {
        super(context);
    }

    /**
     * Send data to a stream which is already defined in Siddhi. This data is accessible via Queries ({@link Query})
     * both by the same Data Agent and other No Data Agents
     *
     * @param stream the stream to which the data will be sent
     * @param data the data to be sent
     * */
    public void sendDataToService(String stream, Object[] data) throws RemoteException {
        super.sendDataToService(stream, data, context.getPackageName());
    }

    /**
     * Add a stream to Siddhi. The stream definition has to be in proper Siddhi QL. For ease of use, try
     * {@link #addStream(Stream)} where the Siddhi QL stream will be defined using the {@link Stream} factory class.
     *
     * @param streamDefinition the stream definition in Siddhi QL
     * */
    public void addStream(String streamDefinition) throws RemoteException {
        String streamName = "";
        Pattern pattern = Pattern.compile("define[\\s]+stream[\\s]+(([a-z]|[A-Z])+).*");
        Matcher matcher = pattern.matcher(streamDefinition);
        while (matcher.find()) {
            streamName = matcher.group(1);
        }
        streams.add(new Stream(streamName, streamDefinition));
        super.addStream(streamDefinition, context.getPackageName());
    }

    /**
     * Add a {@link Query} to Siddhi. The query definition has to be in proper Siddhi QL. For ease of use, try
     * {@link #addQuery(Query)} where the Siddhi QL formatting will be done by the {@link Query} factory class
     *
     * Note: Please note that all query operations are not yet fully supported in {@link Query} factory class.
     * If there is a complex query consider using this method as opposed to {@link #addQuery(Query)}.
     *
     * @param queryDefinition the query definition in Siddhi QL
     * */
    public void addQuery(String queryDefinition) throws RemoteException {
        super.addQuery(queryDefinition, context.getPackageName());
    }

    /**
     * Add a stream to Siddhi. Use the {@link Stream} factory class to build a stream and pass it to Siddhi.
     * The formatting of the Stream into proper Siddhi QL is handled by the {@link Stream} factory class.
     *
     * @param stream the {@link Stream} which is to be created
     * */
    public void addStream(Stream stream) throws RemoteException {
        streams.add(stream);
        super.addStream(stream.create(), context.getPackageName());
    }

    /**
     * Add a {@link Query} to Siddhi. The query definition has to be in proper Siddhi QL. For ease of use, try
     * {@link #addQuery(Query)} where the Siddhi QL formatting will be done by the {@link Query} factory class
     *
     * Note: Please note that all query operations are not yet fully supported in {@link Query} factory class.
     * If there is a complex query please consider using {@link #addQuery(String)}
     *
     * @param query the {@link Query} to be added
     * */
    public void addQuery(Query query) throws RemoteException {
        queries.add(query);
        super.addQuery(query.create(), context.getPackageName());
    }

/*    public void addStreamCallback(String stream, String receiver) throws RemoteException {
        super.addStreamCallback(stream, receiver, context.getPackageName());
    }*/

    public void addCallback(String streamName, String receiver) throws RemoteException {
        super.addStreamCallback(streamName, receiver, context.getPackageName());
    }

    /*public void start() throws RemoteException {
        super.start(context.getPackageName());
    }*/

    /*public void addStreamCallback(String stream, BroadcastReceiver receiver) throws RemoteException {
        super.addStreamCallback(stream, context.getPackageName(), receiver, context.getPackageName());
    }*/

    public void addCallback(String streamName, EdgeAnalyticsCallback callback) throws RemoteException {
        super.addStreamCallback(streamName, context.getPackageName(), callback, context.getPackageName());
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
