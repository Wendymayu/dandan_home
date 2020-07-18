package dandan.wendy.community.enums;

/**
 * Created by codedrinker on 2019/6/14.
 */
public enum NotificationStatusEnum {
    /*表示消息的阅读状态*/
    UNREAD(0), READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
