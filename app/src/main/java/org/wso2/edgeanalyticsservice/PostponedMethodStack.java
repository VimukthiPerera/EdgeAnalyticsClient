package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.RemoteException;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by root on 1/12/16.
 */
public class PostponedMethodStack {
    private static Queue<PostponedMethod> postponedMethodStack = new ArrayDeque<>();
    public static Map<String, BroadcastReceiver> broadcastReceiverMap;
    public static IEdgeAnalyticsService edgeAnalyticsService;
    public static Context context;

    public static void addMethod(String methodName, IEdgeAnalyticsService edgeAnalyticsService, Object... args){
        postponedMethodStack.add(new PostponedMethod(methodName, edgeAnalyticsService, args));
    }

    public static void addMethod(PostponedMethod postponedMethod){
        postponedMethodStack.add(postponedMethod);
    }

    public static void doMethods(Map<String, BroadcastReceiver> broadcastReceiverMap1, Context context1, IEdgeAnalyticsService edgeAnalyticsService1){
        broadcastReceiverMap = broadcastReceiverMap1;
        context = context1;
        edgeAnalyticsService = edgeAnalyticsService1;
        while (!postponedMethodStack.isEmpty()){
            PostponedMethod m = postponedMethodStack.poll();
            try {
                m.doMethod();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
