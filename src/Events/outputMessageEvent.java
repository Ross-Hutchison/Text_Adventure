package Events;

public class outputMessageEvent extends event {

    private String msg;

    /*
    Constructor for limited message output events
     */
    public outputMessageEvent(String type, String msg, int limit, String usedUpMsg) {
        super(type, limit, usedUpMsg);
        this.eventSubType = "outputMessage";
        this.msg = msg;
    }

    /*
    Constructor for unlimited message output events
     */
    public outputMessageEvent(String type, String msg) {
        super(type);
        this.eventSubType = "outputMessage";
        this.msg = msg;
    }

    public String getMsg() { return this.msg; }
}
