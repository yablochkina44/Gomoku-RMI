import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BlackClient extends Client {

    public static final String TAG = "BlackClient";
    protected static final int port = 8138;

    private int mType = Cell.BLACK;

    public BlackClient() throws RemoteException {
        super();
    }

    public void connect() throws RemoteException, NotBoundException, MalformedURLException {
        Registry registry = LocateRegistry.getRegistry(localhost, 8136);
        mServer = (IServer) registry.lookup(Server.TAG);
        mServer.bindAsBlackPlayer();
    }
    // Метод connect устанавливает соединение с удаленным сервером.
    // Он использует RMI-реестр (Registry) для получения ссылки на удаленный объект сервера,
    // а затем вызывает метод bindAsBlackPlayer, который, вероятно,
    // регистрирует текущего клиента как игрока за черных.
    @Override
    public boolean click(int x, int y) throws RemoteException {
        if(!mTurn) return false;
        return mServer.clickFromPlayer(x, y, mType);
    }

    @Override
    public int getType() throws RemoteException {
        return mType;
    }

    public static void main(String[] argv) throws RemoteException, NotBoundException, MalformedURLException{
        //Создание удаленного RMI Объекта.
        BlackClient mClient = new BlackClient();
        // Создается объект BlackClient, который представляет клиентскую сторону игры.
        // В конструкторе BlackClient инициализируется базовая функциональность,
        // включая игровое поле и другие параметры.
        IClient stub = (IClient) UnicastRemoteObject.exportObject(mClient, port);
        // UnicastRemoteObject.exportObject экспортирует объект для удаленных вызовов методов.
        // В данном случае, mClient экспортируется как объект, реализующий интерфейс IClient. Получается "stub" (заглушка),
        // которая используется для создания удаленного объекта, который клиент может вызывать.
        System.out.println("Initializing " + TAG);

        //Регистрация удаленного RMI Объекта в реестре
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(TAG, stub);
        // LocateRegistry.createRegistry создает RMI-реестр (Registry)
        // на указанном порту (в данном случае, port). registry.rebind(TAG, stub)
        // регистрирует объект stub (заглушку) под именем TAG в реестре.
        // Теперь клиентский объект становится доступным для удаленных вызовов через RMI.
        System.out.println("Starting " + TAG);
        mClient.connect();
    }
    //Метод main создает экземпляр BlackClient, экспортирует его как RMI-объект, регистрирует его в локальном RMI-реестре, и затем устанавливает соединение с сервером.
    //
    //Этот класс представляет собой реализацию клиента для игрока,
    // который играет за черных. Он использует RMI для взаимодействия с удаленным сервером,
    // обрабатывает кликовые события и обновляет состояние игры на стороне клиента.
}
