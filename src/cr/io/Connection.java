package cr.io;

import cr.LocalEnum;
import cr.events.Event;
import cr.util.user.UserInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Bobbywang
 * @date 2021-10-16 16:48
 */
public final class Connection {
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Connection(Socket socket, UserInfo type) {
        this.socket = socket;
        try {
            if (type.equals(LocalEnum.SERVER)) {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } else {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(Event m) {
        try {
            out.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Event readMessage() throws IOException, ClassNotFoundException {
        return (Event) in.readObject();
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
