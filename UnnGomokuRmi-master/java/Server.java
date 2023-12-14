import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements IServer {

    public static final String TAG = "Server";
    private static final String localhost = "127.0.0.1";
    private static final int port = 8136;
    private static final int WIN_COUNT = 6;

    private Field mField;
    private IClient mWhitePlayer = null;
    private IClient mBlackPlayer = null;

    public Server(){
        mField = new Field(Field.DEFAULT_COUNT);
    }

    @Override
    public boolean bindAsWhitePlayer() throws RemoteException, NotBoundException, MalformedURLException {
        if (mWhitePlayer!= null) return false;
        Registry registry = LocateRegistry.getRegistry(localhost, 8137);
        mWhitePlayer = (IClient) registry.lookup(WhiteClient.TAG);
        System.out.println("White player is connected!");
        if (mBlackPlayer != null) {
            mWhitePlayer.startPlay(true);
            mBlackPlayer.startPlay(false);
        }

        return true;
    }
    // Метод регистрирует клиента (белого игрока) в сервере.
    // Если черный игрок уже зарегистрирован, оба игрока получают уведомление о начале игры.
    @Override
    public boolean bindAsBlackPlayer() throws RemoteException, NotBoundException, MalformedURLException {
        if (mBlackPlayer != null) return false;
        Registry registry = LocateRegistry.getRegistry(localhost, 8138);
        mBlackPlayer = (IClient) registry.lookup(BlackClient.TAG);
        System.out.println("Black player is connected!");
        if (mBlackPlayer != null) {
            mWhitePlayer.startPlay(true);
            mBlackPlayer.startPlay(false);
        }
        return true;
    }
    @Override
    public boolean clickFromPlayer(int aX, int aY, int aType) throws RemoteException {
        boolean isOkay = mField.setCellType(aX, aY, aType);
        System.out.println("Click From Player");
        if (isOkay) {
            if(aType == Cell.WHITE) {
                mBlackPlayer.setTurn(true);
                if (checkWin(aX, aY, aType)) {
                    System.out.println("White Win!");
                    mWhitePlayer.setWin(true);
                    mBlackPlayer.setWin(false);
                    mWhitePlayer.stopGame();
                    mBlackPlayer.stopGame();
                }
                mWhitePlayer.updateCell(aX, aY, aType);
                mBlackPlayer.updateCell(aX, aY, aType);
            } else if (aType == Cell.BLACK) {
                mWhitePlayer.setTurn(true);
                if (checkWin(aX, aY, aType)) {
                    System.out.println("Black Win!");
                    mWhitePlayer.setWin(false);
                    mBlackPlayer.setWin(true);
                    mWhitePlayer.stopGame();
                    mBlackPlayer.stopGame();
                }
                mWhitePlayer.updateCell(aX, aY, aType);
                mBlackPlayer.updateCell(aX, aY, aType);
            }
        }
        return isOkay;
    }
    //Метод обрабатывает клик от игрока, обновляет состояние игры и проверяет наличие победителя.

    private boolean checkWin(int aX, int aY, int aType) {
        //Check diagonal
        if (checkLeftDiagonal(aX, aY, aType)) return true;
        if (checkRightDiagonal(aX, aY, aType)) return true;
        //Check line
        if (checkXLine(aX, aY, aType)) return true;
        if (checkYLine(aX, aY, aType)) return true;
        System.out.println("Not Win!");
        return false;
    }
    private boolean checkLeftDiagonal(int aX, int aY, int aType) {
        if(mField.getCellType(aX, aY) != aType) {
            return false;
        }
        int count = 1;
        int tmpX = aX - 1;
        int tmpY = aY - 1;
        while(mField.getCellType(tmpX, tmpY) == aType) {
            count++;
            tmpX--;
            tmpY--;
        }
        tmpX = aX + 1;
        tmpY = aY + 1;
        while(mField.getCellType(tmpX, tmpY) == aType) {
            count++;
            tmpX++;
            tmpY++;
        }
        return count == WIN_COUNT;
    }
    private boolean checkRightDiagonal(int aX, int aY, int aType) {
        if(mField.getCellType(aX, aY) != aType) {
            return false;
        }
        int count = 1;
        int tmpX = aX + 1;
        int tmpY = aY - 1;
        while(mField.getCellType(tmpX, tmpY) == aType) {
            count++;
            tmpX++;
            tmpY--;
        }
        tmpX = aX - 1;
        tmpY = aY + 1;
        while(mField.getCellType(tmpX, tmpY) == aType) {
            count++;
            tmpX--;
            tmpY++;
        }
        return count == WIN_COUNT;
    }
    private boolean checkXLine(int aX, int aY, int aType) {
        if(mField.getCellType(aX, aY) != aType) {
            return false;
        }
        int count = 1;
        int tmpX = aX - 1;
        while (mField.getCellType(tmpX, aY) == aType) {
            count++;
            tmpX--;
        }
        tmpX = aX + 1;
        while (mField.getCellType(tmpX, aY) == aType) {
            count++;
            tmpX++;
        }
        return count == WIN_COUNT;
     }
    private boolean checkYLine(int aX, int aY, int aType) {
        if(mField.getCellType(aX, aY) != aType) {
            return false;
        }
        int count = 1;
        int tmpY = aY - 1;
        while (mField.getCellType(aX, tmpY) == aType) {
            count++;
            tmpY--;
        }
        tmpY = aY + 1;
        while (mField.getCellType(aX, tmpY) == aType) {
            count++;
            tmpY++;
        }
        return count == WIN_COUNT;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        //Создание удаленного RMI Объекта.
        Server server = new Server();
        IServer stub = (IServer) UnicastRemoteObject.exportObject(server, port);
        System.out.println("Initializing Server");

        //Регистрация удаленного RMI Объекта в реестре
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(TAG, stub);
        System.out.println("Server is ready!");
    }
    //Метод main создает экземпляр сервера, экспортирует его как RMI-объект и регистрирует в RMI-реестре.
    // Таким образом, сервер становится доступным для удаленных вызовов методов.
}

