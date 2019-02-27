/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.y.b.tools;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more efficient means
 * such as through injection directly into interested classes.
 */
public final class BusProvider {

    private static BusProvider mInstance;
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);
    private static final String LOG_CAT = "MxBusProvider";

    public static BusProvider getInstance() {
        if (mInstance == null) {
            mInstance = new BusProvider();
        }
        return mInstance;
    }

    private BusProvider() {

    }

    public void register(Object object) {
        log(object.toString() + " registered");
        BUS.register(object);
    }

    public void unregister(Object object) {
        log(object.toString() + " unRegistered");
        BUS.unregister(object);
    }

    public void post(Object event) {
        BUS.post(event);
    }

    private void log(String msg) {
        Log.d(LOG_CAT, msg);
    }

//    /**
//     * 在UI线程中发送Event消息
//     * @param event
//     */
//    public static void sendBusEventOnUiThread(final Object event){
//        MxTaskManager.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                getInstance().post(event);
//            }
//        });
//    }
//
//    /**
//     * 在UI线程中延迟发送Event
//     * @param delay
//     * @param event
//     */
//    public static void sendBusEventOnUiThreadDelay(long delay, final Object event){
//        MxTaskManager.runOnUiThreadDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getInstance().post(event);
//            }
//        }, delay);
//    }
}
