package thread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static thread.ShipModel.shipType.*;


/** @shipType - тип корабля */
/** @capacity - вместимость корабля */
/** @shipNumber - порядковый номер корабля */
class ShipModel {
    public enum shipType {
        ХЛЕБ,
        БАНАН,
        ОДЕЖДА
    }

    private shipType type;
    private int capacity;
    private int shipNumber;

    public ShipModel(shipType type, int capacity, int shipNumber) {
        this.type = type;
        this.capacity = capacity;
        this.shipNumber = shipNumber;
    }

    public shipType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumber() {
        return shipNumber;
    }
}

class TunnelModel {
    // TODO: Через Семафоры осуществляется борьба за РЕСУРСЫ!!!!
    private Semaphore semaphore;

    // TODO: счетчик для доступа к ресурсам
    public TunnelModel(int maxShips) {
        semaphore = new Semaphore(maxShips);
    }

    /** Вход в туннель */
    public void enterTunnel() throws InterruptedException {
        semaphore.acquire();
    }

    /** Выход из туннеля */
    public void exitTunnel() {
        semaphore.release();
    }
}

class DockModel {
    private ShipModel.shipType type;

    public DockModel(ShipModel.shipType type) {
        this.type = type;
    }

    /** Загрузка корабля */
    public void loadShip(ShipModel ship) {
        System.out.println("Загружаем товары" + " " + ship.getType() + "-"+ ship.getNumber()+ " " + "корабль...");

        int remainingCapacity = ship.getCapacity();
        while (remainingCapacity > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            remainingCapacity -= 10;
            System.out.println(type +" " + "причал загрузил 10 товаров для" +" "+ ship.getType() + "-" + ship.getNumber()+" " + "корабля. Осталось загрузить" + " " + remainingCapacity + "товаров");
        }
        System.out.println(ship.getType() +  "-" +ship.getNumber()+ "корабль загружен" + " " + type +" " + "причалом");
    }
}


public class Main {
    public static void main(String[] args)  {
        TunnelModel tunnel = new TunnelModel(5);
        DockModel breadDock = new DockModel(ХЛЕБ);
        DockModel bananaDock = new DockModel(БАНАН);
        DockModel clothesDock = new DockModel(ОДЕЖДА);

        ExecutorService executor = Executors.newFixedThreadPool(10); // Пул потоков для генерации кораблей

        // Генерируем 10 кораблей
        for (int i = 0; i < 10; i++) {
            int randomShipType = i % 3; // 0, 1 или 2
            ShipModel.shipType shipType = null;
            // минимальная вместимость корабля
            final int MIN = 10;
            // максимальная вместимость корабля
            final int MAX = 100;
            Random random = new Random();
            // TODO: Генерируем рандомную вместимость:[10;100] с шагом 10
            int capacity = random.nextInt((MAX - MIN) /10 + 1)* 10 + MIN;
            // TODO: Генериурем вид корабля по номеру 1,2,3
            switch (randomShipType) {
                case 0:    shipType = ХЛЕБ;
                    break;

                case 1:    shipType = БАНАН;
                    break;

                case 2:    shipType = ОДЕЖДА;
                    break;

                default: throw new IllegalStateException("Нет такого корабля!" +" "+ shipType);
            }


            ShipModel ship = new ShipModel(shipType, capacity, i);

            executor.submit(() -> {
                try {
                    tunnel.enterTunnel();
                    // За счет засыпания обеспечиваем генерацию разных кораблей
                    Thread.sleep(capacity);
                    DockModel dock;
                    // TODO: формируем причалы для кораблей
                    switch (ship.getType()){
                        case ХЛЕБ: dock = breadDock;
                            break;

                        case БАНАН: dock = bananaDock;
                            break;

                        case ОДЕЖДА: dock = clothesDock;
                            break;

                        default: throw new IllegalStateException("Нет такого причала!" +" "+ ship.getType());
                    }
                    dock.loadShip(ship);
                    tunnel.exitTunnel();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}