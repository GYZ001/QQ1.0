package listener;

import pub.TCPMessage;

public interface TCPListener {
    public void execute(TCPMessage tcpMessage);
}
