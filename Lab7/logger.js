const { createLogger, format, transports } = require('winston');
const DailyRotateFile = require('winston-daily-rotate-file');

const logger = createLogger({
    level: 'info',
    format: format.combine(
        format.timestamp(),
        format.printf(({ level, message, timestamp }) => {
            return `${timestamp} [${level.toUpperCase()}]: ${message}`;
        })
    ),
    transports: [
        new transports.Console(), // Логи выводятся в консоль
        new DailyRotateFile({
            filename: '/app/logs/app-%DATE%.log', // Шаблон имени файлов
            datePattern: 'YYYY-MM-DD-HH',
            zippedArchive: true,
            maxSize: '10m',
            maxFiles: '14d'
        }),
    ],
});

module.exports = logger;