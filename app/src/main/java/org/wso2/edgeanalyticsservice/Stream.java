package org.wso2.edgeanalyticsservice;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;
import java.util.Map;

//TODO add javadoc wrap all types of streams

/**
 * Created by root on 12/12/15.
 */
public class Stream implements Parcelable {
    private String streamName;
    private Map<String, String> dataType;
    private String streamDefinition;

    public Stream(){
        dataType = new LinkedHashMap<>();
    }

    public Stream(String streamName) {
        this.streamName = streamName;
        dataType = new LinkedHashMap<>();
    }

    protected Stream(Parcel in) {
        streamName = in.readString();
        streamDefinition = in.readString();
    }

    public static final Creator<Stream> CREATOR = new Creator<Stream>() {
        @Override
        public Stream createFromParcel(Parcel in) {
            return new Stream(in);
        }

        @Override
        public Stream[] newArray(int size) {
            return new Stream[size];
        }
    };

    public Stream streamName(String name){
        streamName = name;
        return this;
    }

    public Stream addData(String name, String type){
        dataType.put(name, type);
        return this;
    }

    public Stream(String streamName, String streamDefinition) {
        this.streamName = streamName;
        this.streamDefinition = streamDefinition;
        dataType = new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        return streamName + " : " + streamDefinition;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getStreamDefinition() {
        return streamDefinition;
    }

    public Map<String, String> getDataType() {
        return dataType;
    }

    public String create(){
        StringBuilder sb = new StringBuilder();
        sb.append("define stream ");
        sb.append(streamName);
        sb.append("(");
        int i = 0;
        for(String name: dataType.keySet()){
            sb.append(name+" "+dataType.get(name));
            i++;
            if(i<dataType.size()) {
                sb.append(",");
            }
        }
        sb.append("); ");
        streamDefinition = sb.toString();
        return streamDefinition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(streamName);
        dest.writeString(streamDefinition);
        dest.writeIntArray(new int[0]);
    }
}
