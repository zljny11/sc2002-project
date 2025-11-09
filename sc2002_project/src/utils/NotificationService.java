package utils;

/**
 * Simple notification service that prints messages to console.
 * Used to display system notifications to users.
 */
public class NotificationService {

	/**
	 * Displays a notification message with [Notification] prefix.
	 * @param message the message to display
	 */
	public static void notify(String message) { System.out.println("[Notification] " + message); }

}
