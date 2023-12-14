import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class WhiteClient extends Client {

    public static final String TAG = "WhiteClient";
    protected static final int port = 8137;

    private int mType = Cell.WHITE;

    public WhiteClient() throws RemoteException {
        super();
    }

    public void connect() throws RemoteException, NotBoundException, MalformedURLException {
        Registry registry = LocateRegistry.getRegistry(localhost, 8136);
        mServer = (IServer) registry.lookup(Server.TAG);
        mServer.bindAsWhitePlayer();
    }

    @Override
    public boolean click(int x, int y) throws RemoteException {
        if(!mTurn) return false;
        return mServer.clickFromPlayer(x, y, mType);
    }

    @Override
    public int getType() throws RemoteException {
        return mType;
    }

    public static void main(String[] argv) throws RemoteException, NotBoundException, MalformedURLException {
        //Создание удаленного RMI Объекта.
        WhiteClient mClient = new WhiteClient();
        IClient stub = (IClient) UnicastRemoteObject.exportObject(mClient, port);
        System.out.println("Initializing " + TAG);

        //Регистрация удаленного RMI Объекта в реестре
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(TAG, stub);
        System.out.println("Starting " + TAG);
        mClient.connect();
    }
}
