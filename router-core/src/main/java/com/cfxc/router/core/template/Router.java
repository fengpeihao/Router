package com.cfxc.router.core.template;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.cfxc.router.annotation.Constants;
import com.cfxc.router.annotation.model.RouteMeta;
import com.cfxc.router.annotation.utils.Utils;
import com.cfxc.router.core.Postcard;
import com.cfxc.router.core.Warehouse;
import com.cfxc.router.core.callback.InterceptorCallback;
import com.cfxc.router.core.callback.NavigationCallback;
import com.cfxc.router.core.implments.InterceptorImpl;
import com.cfxc.router.core.utils.ClassUtils;
import com.cfxc.router_core.BuildConfig;
import com.cfxc.router_core.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static com.cfxc.router.annotation.Constants.KEY_DESTINATION_ID;
import static com.cfxc.router.annotation.Constants.PACKAGE_OF_GENERATE_FILE;
import static com.cfxc.router.annotation.Constants.PROJECT;
import static com.cfxc.router.annotation.Constants.SEPARATOR;
import static com.cfxc.router.annotation.Constants.SUFFIX_INTERCEPTOR;
import static com.cfxc.router.annotation.Constants.SUFFIX_ROOT;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/6/21
 */
public class Router {

    private static final String TAG = "Router";
    private static Router sInstance;
    private static Application sApplication;
    private static Handler mHandler;

    public static Router getInstance() {
        if (sInstance == null) {
            synchronized (Router.class) {
                if (sInstance == null) {
                    sInstance = new Router();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        sApplication = application;
        mHandler = new Handler(Looper.getMainLooper());
        try {
            loadInfo();
            InterceptorImpl.init(application);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init failed!", e);
        }
    }

    private static void loadInfo() throws PackageManager.NameNotFoundException, InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(sApplication, PACKAGE_OF_GENERATE_FILE);
        for (String className : routerMap) {
            if (className.startsWith(PACKAGE_OF_GENERATE_FILE + "." + PROJECT + SEPARATOR + SUFFIX_ROOT)) {
                ((IRouteRoot) Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.routes);
            } else if (className.startsWith(PACKAGE_OF_GENERATE_FILE + "." + PROJECT + SEPARATOR + SUFFIX_INTERCEPTOR)) {
                ((IInterceptorRoot) Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.interceptorsIndex);
            }
        }
    }

    public Postcard build(String path) {
        RouteMeta routeMeta = Warehouse.routes.get(path);
        if (routeMeta == null) return new Postcard(RouteMeta.RouteType.FRAGMENT, "", "", null);
        return new Postcard(routeMeta.getType(), routeMeta.getDestinationText(), routeMeta.getGraphText(), routeMeta.getDestination());
    }

    public Object navigation(NavController navController, Postcard postcard, String popUpToDestination, boolean inclusive, NavigationCallback callback) {
        switch (postcard.getType()) {
            case FRAGMENT:
                InterceptorImpl.onInterceptions(postcard, new InterceptorCallback() {
                    /**
                     * Continue process
                     *
                     * @param postcard route meta
                     */
                    @Override
                    public void onContinue(Postcard postcard) {
                        runInMainThread(() -> _navigation(navController, postcard, popUpToDestination, inclusive, callback));
                    }

                    /**
                     * Interrupt process, pipeline will be destroy when this method called.
                     *
                     * @param exception Response of interrupt.
                     */
                    @Override
                    public void onInterrupt(Throwable exception) {
                        if (null != callback) callback.onInterrupt(postcard);
                    }
                });
                break;
            case PROVIDER:
                if (Warehouse.providerMap.get(postcard.getDestination()) == null) {
                    try {
                        Warehouse.providerMap.put(postcard.getDestination(), (IProvider) postcard.getDestination().getConstructor().newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return Warehouse.providerMap.get(postcard.getDestination());
            default:
                break;
        }
        return null;
    }

    private Object _navigation(NavController navController, Postcard postcard, String
            popUpToDestination, boolean inclusive, NavigationCallback callback) {
        try {
            if (!isNeedPrerequisite(navController, postcard, popUpToDestination, inclusive)) {
                navigate(navController, postcard.getGraphText(), postcard.getDestinationText(), popUpToDestination, inclusive, postcard.getBundle());
            }
            if (null != callback) callback.onArrival(postcard);

        } catch (Exception e) {
            if (null != callback) callback.onLost(postcard);
            Log.e(TAG, e.getMessage());
            return null;
        }
        return null;
    }

    private boolean isNeedPrerequisite(NavController navController, Postcard postcard, String
            popUpToDestination, boolean inclusive) {
        String prerequisiteDestination = postcard.getPrerequisiteDestination();
        if (Utils.isNotEmpty(prerequisiteDestination)) {
            Bundle bundle = postcard.getBundle();
            if (bundle == null) bundle = new Bundle();
            bundle.putString(Constants.KEY_CONTINUE_DESTINATION, postcard.getDestinationText());
            navigate(navController, postcard.getPrerequisiteDestinationGraph(), prerequisiteDestination, popUpToDestination, inclusive, bundle);
            return true;
        } else {
            return false;
        }
    }

    private void navigate(NavController navController, String graphText, String
            destinationText, String popUpToDestination, boolean inclusive, Bundle bundle) {
        if (navController == null) {
            Toast.makeText(sApplication, "The NavController must not be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        int destinationId = getDestinationId(destinationText);
        NavOptions.Builder navOptions = new NavOptions.Builder();
        if (Utils.isNotEmpty(popUpToDestination))
            navOptions.setPopUpTo(getDestinationId(popUpToDestination), inclusive);
        navOptions.setEnterAnim(R.anim.slide_in).setExitAnim(R.anim.slide_out);
        if (bundle == null) bundle = new Bundle();
        if (navController.getGraph().getId() == getDestinationId(graphText)) {
            navController.navigate(destinationId, bundle, navOptions.build());
        } else {
            bundle.putInt(KEY_DESTINATION_ID, destinationId);
            navController.navigate(getDestinationId(graphText), bundle, navOptions.build());
        }
    }

    public static boolean debuggable() {
        return BuildConfig.DEBUG;
    }

    private int getDestinationId(String destinationText) {
        if (Warehouse.destinationMap.get(destinationText) != null && Warehouse.destinationMap.get(destinationText) != 0) {
            return Warehouse.destinationMap.get(destinationText);
        } else {
            int destinationId = sApplication.getResources().getIdentifier(destinationText, "id", sApplication.getPackageName());
            Warehouse.destinationMap.put(destinationText, destinationId);
            return destinationId;
        }
    }

    /**
     * Be sure execute in main thread.
     *
     * @param runnable code
     */
    private void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }
}
