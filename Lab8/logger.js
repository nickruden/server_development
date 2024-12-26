const winston = require('winston');
const { combine, timestamp, printf, colorize } = winston.format;

// Определяем формат логов
const logFormat = printf(({ level, message, timestamp, consumerId }) => {
    const consumerInfo = consumerId ? `[Консюмер ${consumerId}] ` : '';
    return `${timestamp} [${level}]: ${consumerInfo}${message}`;
  });

// Создаём логгер
const logger = winston.createLogger({
  level: 'info', // Уровень логирования (info, error, warn, debug)
  format: combine(
    timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }), // Добавляем временную метку
    colorize(), // Добавляем цвета для уровня логирования
    logFormat // Используем наш формат
  ),
  transports: [
    // Логирование в консоль
    new winston.transports.Console(),
    // Логирование в файл
    new winston.transports.File({ filename: 'logs/app.log' })
  ]
});

logger.withConsumer = (consumerId) => {
    return logger.child({ consumerId });
};

module.exports = logger;