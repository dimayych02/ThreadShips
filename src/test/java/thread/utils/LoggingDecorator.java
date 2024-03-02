package thread.utils;


import java.util.logging.Level;
import java.util.logging.Logger;

// Класс - синглтон , предназначенный для логгирования сущностей
public class LoggingDecorator {
    private static final Logger LOGGER = Logger.getLogger(LoggingDecorator.class.getName());
    private static LoggingDecorator instance;

    // Приватный конструктор, чтобы предотвратить создание экземпляров извне
    private LoggingDecorator() {
    }

    public static LoggingDecorator getInstance() {
        if (instance == null) {
            // Потокобезопасность
            synchronized (LoggingDecorator.class) {
                if (instance == null) {
                    instance = new LoggingDecorator();
                }
            }
        }
        return instance;
    }

    // Логгирование методов
    public void logInfo(String methodName) throws Throwable {
        try {
            LOGGER.info("Метод:" + " " + methodName);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Ошибка при выполнении метода:" + " " + methodName + ex.getCause().getMessage());
            throw ex.getCause();
        }
    }

}


