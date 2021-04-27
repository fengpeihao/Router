package com.cfxc.router.core.implments;

import android.content.Context;

import com.cfxc.router.core.Postcard;
import com.cfxc.router.core.Warehouse;
import com.cfxc.router.core.callback.InterceptorCallback;
import com.cfxc.router.core.exception.HandlerException;
import com.cfxc.router.core.template.IInterceptor;
import com.cfxc.router.core.thread.CancelableCountDownLatch;
import com.cfxc.router.core.thread.DefaultPoolExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/2/21
 */
public class InterceptorImpl {

    private volatile static ThreadPoolExecutor executor = DefaultPoolExecutor.getInstance();

    public static void init(final Context context) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (Warehouse.interceptorsIndex != null && !Warehouse.interceptorsIndex.isEmpty()) {
                    for (Map.Entry<Integer, Class<? extends IInterceptor>> entry : Warehouse.interceptorsIndex.entrySet()) {
                        Class<? extends IInterceptor> interceptorClass = entry.getValue();
                        try {
                            IInterceptor iInterceptor = interceptorClass.getConstructor().newInstance();
                            iInterceptor.init(context);
                            Warehouse.interceptors.add(iInterceptor);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void onInterceptions(final Postcard postcard, final InterceptorCallback callback) {
        if (Warehouse.interceptors.size() > 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    CancelableCountDownLatch countDownLatch = new CancelableCountDownLatch(Warehouse.interceptors.size());
                    try {
                        execute(0, countDownLatch, postcard);
                        countDownLatch.await(postcard.getTimeout(), TimeUnit.SECONDS);
                        if (countDownLatch.getCount() > 0) { // Cancel the navigation this time, if it hasn't return anythings.
                            callback.onInterrupt(new HandlerException("The interceptor processing timed out."));
                        } else if (null != postcard.getTag()) { // Maybe some exception in the tag.
                            callback.onInterrupt((Throwable) postcard.getTag());
                        } else {
                            callback.onContinue(postcard);
                        }
                    } catch (InterruptedException e) {
                        callback.onInterrupt(e);
                    }
                }
            });
        } else {
            callback.onContinue(postcard);
        }
    }

    /**
     *
     * @param index current interceptor index
     * @param countDownLatch interceptor counter
     * @param postcard routeMeta
     */
    private static void execute(final int index, final CancelableCountDownLatch countDownLatch, final Postcard postcard) {
        if (index < Warehouse.interceptors.size()) {
            IInterceptor iInterceptor = Warehouse.interceptors.get(index);
            iInterceptor.process(postcard, new InterceptorCallback() {
                @Override
                public void onContinue(Postcard postcard) {
                    // Last interceptor execute over with no exception.

                    countDownLatch.countDown();
                    // When counter is down, it will be execute continue ,but index bigger than interceptors size, then U know.
                    execute(index + 1, countDownLatch, postcard);
                }

                @Override
                public void onInterrupt(Throwable exception) {
                    // Last interceptor execute over with fatal exception.

                    postcard.setTag(null == exception ? new HandlerException("No message.") : exception);    // save the exception message for backup.
                    countDownLatch.cancel();
                }
            });
        }
    }
}
































