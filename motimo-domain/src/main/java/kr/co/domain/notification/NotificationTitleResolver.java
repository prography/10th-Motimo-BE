package kr.co.domain.notification;

import java.util.Map;

public class NotificationTitleResolver {

    /**
     * @param type NotificationType
     * @param variables 치환할 값들 (key: placeholder 이름, value: 치환할 값)
     */
    public static String resolveTitle(NotificationType type, Map<String, String> variables) {
        String result = type.getDefaultTitle();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }

        return result;
    }
}