package mario.peric.binders;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import mario.peric.INotificationBinder;
import mario.peric.INotificationCallback;

public class NotificationBinder extends INotificationBinder.Stub {
    INotificationCallback mNotificationCallBack;
    NotificationCaller mNotificationCaller;

    @Override
    public void setCallback(INotificationCallback callback) throws RemoteException {
        mNotificationCallBack = callback;
        mNotificationCaller = new NotificationCaller();
        mNotificationCaller.start();
    }

    public void stop() {
        mNotificationCaller.stop();
    }

    private class NotificationCaller implements Runnable {

        public static final long PERIOD = 5000L;

        private Handler mHandler = null;
        private boolean mRun = true;

        @Override
        public void run() {
            if (!mRun) {
                return;
            }
            try {
                mNotificationCallBack.onCallbackCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mHandler.postDelayed(this, PERIOD);
        }

        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(this, PERIOD);
            mRun = true;
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }
    }
}
