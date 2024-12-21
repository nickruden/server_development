const logger = require('../logger'); // Убедитесь, что путь правильный
const Sequelize = require('sequelize');

// Конфигурация подключения к MySQL
const sequelize = new Sequelize(
    process.env.MYSQL_DB, // Имя базы данных
    process.env.MYSQL_USER, // Имя пользователя
    process.env.MYSQL_PASSWORD, // Пароль
    {
        host: process.env.MYSQL_HOST, // Хост базы данных
        dialect: 'mysql', // Используем MySQL
        logging: (msg) => logger.info(msg) // Использование logger для SQL-запросов
    }
);

module.exports = sequelize;

// Проверка подключения с логированием ошибок
sequelize
    .authenticate()
    .then(() => {
        logger.info('Database connected successfully');
    })
    .catch((err) => {
        logger.error(`Database connection error: ${err.message}`);
        logger.error(err); // Логирование полной ошибки
    });