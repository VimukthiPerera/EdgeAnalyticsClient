package org.wso2.edgeanalyticsservice;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

//TODO add javadoc, wrap all types of queries

/**
 * Created by root on 12/17/15.
 */
public class Query {

    private static int count = 0;
    private String queryName;
    private String inStream;
    private String outStream = "";
    private String queryDefinition;
    private String condition = "";
    private String window = "";
    private List<String> data;
    private List<String> conditions;

    public Query() {
        data = new ArrayList<>();
    }

/*    public Query defineQuery(String queryDefinition){
        this.queryDefinition = queryDefinition;
        return this;
    }*/

    public Query queryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public Query fromStream(String streamName) {
        this.inStream = streamName;
        return this;
    }

    public Query outStream(String streamName) {
        this.outStream = streamName;
        return this;
    }

    public Query selectData(String data) {
        this.data.add(data);
        return this;
    }

    public Query condition(String condition) {
        this.condition = condition;
        return this;
    }

    public Query condition(String condition, String window) {
        this.condition = condition;
        this.window = window;
        return this;
    }

    public Query addCondition(String stream, String operation, String condition) throws Exception{
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (stream != null || !stream.isEmpty())
            conditions.add(operation + " " + stream + "[" + condition + "] ");
        else if(inStream != null || !inStream.isEmpty())
            conditions.add(operation + " " + inStream + "[" + condition + "] ");
        else
            throw new UnsupportedOperationException("Please define default from stream using fromStream() or enter a not null stream name in addCondition()");
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(queryName != null && !queryName.isEmpty()) {
            sb.append("@info(name='" + queryName + "') ");
        } else {
            sb.append("@info(name='UnnamedQuery" + count++ + "') ");
        }
        if (condition != null && !condition.isEmpty()) {
            sb.append("from " + inStream + "[" + condition + "] " + window);
        } else if (conditions != null) {
            for (String conditionStr : conditions) {
                sb.append(conditionStr);
            }
        } else {
            sb.append("from ").append(inStream);
        }
        sb.append(" select ");
        int i = 0;
        for (String data : this.data) {
            sb.append(data);
            i++;
            if (i < this.data.size()) {
                sb.append(",");
            }
        }
        if (data.size() == 0) {
            sb.append("*");
        }
        sb.append(" insert into ");
        if (outStream != null && !outStream.isEmpty()) {
            sb.append(outStream + " ;");
        } else {
            sb.append("outputStream" + count + " ;");
            synchronized (this) {
                count++;
            }
        }
        queryDefinition = sb.toString();
        return queryDefinition;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getOutStream() {
        return outStream;
    }

    public String getInStream() {
        return inStream;
    }

    public String getQueryDefinition() {
        return queryDefinition;
    }

    public List<String> getData() {
        return data;
    }

    public String create() {
        return toString();
    }
}
