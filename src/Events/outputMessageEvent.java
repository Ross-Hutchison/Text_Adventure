package Events;

public class outputMessageEvent extends event {

    private String msg;

    /*
    Constructor for limited message output events
     */
    public outputMessageEvent(String type, String interactionType, String msg, int limit, String usedUpMsg) {
        super(type, interactionType, limit, usedUpMsg);
        this.msg = msg;
    }

    /*
    Constructor for unlimited message output events
     */
    public outputMessageEvent(String type, String interactionType, String msg) {
        super(type, interactionType);
        this.msg = msg;
    }

    public String getMsg() { return this.msg; }
}
