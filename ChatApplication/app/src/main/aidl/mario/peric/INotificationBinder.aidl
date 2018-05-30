// INotificationBinder.aidl
package mario.peric;

// Declare any non-default types here with import statements
import mario.peric.INotificationCallback;

interface INotificationBinder {

    void setCallback(in INotificationCallback callback);

}
