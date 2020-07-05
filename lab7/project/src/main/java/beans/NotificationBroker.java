package beans;

import entities.BorrowingAvailableNotification;

import java.util.HashMap;
import java.util.Map;

public class NotificationBroker {
    private Map<Integer, LoginManager> listeners = new HashMap<>();

    private static NotificationBroker instance = null;
    public static NotificationBroker getInstance() {
        if (instance == null) {
            instance = new NotificationBroker();
        }
        return instance;
    }

    public void addNotification(BorrowingAvailableNotification notification) {
        int readerId = notification.getReader().getId();
        listeners.get(readerId).processNotification(notification);
    }

    public void addListener(LoginManager loginManager) {
        listeners.put(loginManager.getReader().getId(), loginManager);
    }
}
