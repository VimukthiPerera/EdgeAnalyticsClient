package org.wso2.edgeanalyticsservice;

/**
 * The context for all WSO2 services.
 * <ul>
 *     <li>EDGE_ANALYTICS_SERVICE_DATA : To get an instance of {@link DataAgentManager}</li>
 *     <li>EDGE_ANALYTICS_SERVICE_NO_DATA : To get an instance of {@link NoDataAgentManager}</li>
 * </ul>
 */
public class WSO2Context {
//    public static final String EDGE_ANALYTICS_SERVICE_DEFAULT = "Duplex";
    public static final String EDGE_ANALYTICS_SERVICE_DATA = "Data";
    public static final String EDGE_ANALYTICS_SERVICE_NO_DATA = "NoData";
}
