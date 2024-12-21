const express = require('express');
const logger = require('./logger');

const app = express();
const PORT = 4000;

// Middleware для логирования HTTP-запросов
app.use((req, res, next) => {
  logger.http(`HTTP Request: ${req.method} ${req.url}`);
  next();
});

// Главная страница
app.get('/', (req, res) => {
  logger.info('User visited the home page');
  res.send(`
    <h1>Добро пожаловать на главную страницу</h1>
    <p>Исследуйте следующие страницы:</p>
    <ul>
      <li><a href="/about">О нас</a></li>
      <li><a href="/warn">Вызвать предупреждение</a></li>
      <li><a href="/error">Вызвать ошибку</a></li>
      <li><a href="/form">Отправить форму</a></li>
      <li><a href="/debug">Просмотреть дебаг-лог</a></li>
    </ul>
  `);
});

// Страница "О нас"
app.get('/about', (req, res) => {
  logger.info('User visited the about page');
  res.send(`
    <h1>О нас</h1>
    <p>Это простой пример логирования с использованием Winston и Express.</p>
    <a href="/">Вернуться на главную</a>
  `);
});

// Страница для записи предупреждения
app.get('/warn', (req, res) => {
  logger.warn('User triggered a warning log');
  res.send(`
    <h1>Предупреждение записано!</h1>
    <p>Предупреждение было записано в консоль и файл.</p>
    <a href="/">Вернуться на главную</a>
  `);
});

// Страница для записи ошибки
app.get('/error', (req, res) => {
  try {
    throw new Error('Simulated error occurred');
  } catch (err) {
    logger.error(`Error occurred: ${err.message}`);
    res.send(`
      <h1>Предупреждение записано!</h1>
      <p>Предупреждение было записано в консоль и файл.</p>
      <a href="/">Вернуться на главную</a>
    `);
  }
});

// Страница с формой
app.get('/form', (req, res) => {
  logger.info('User accessed the form page');
  res.send(`
    <h1>Страница с формой</h1>
    <p>Введите ваше имя, чтобы увидеть логирование в действии.</p>
    <form method="POST" action="/form">
      <input type="text" name="name" placeholder="Введите ваше имя"/>
      <button type="submit">Отправить</button>
    </form>
    <a href="/">Вернуться на главную</a>
  `);
});

// Обработка POST-запроса формы
app.use(express.urlencoded({ extended: true }));
app.post('/form', (req, res) => {
  const { name } = req.body;

  // Проверка на пустое значение
  if (!name) {
    logger.error('User did not enter a name');
    return res.status(400).send(`
      <h1>Ошибка: Имя обязательно!</h1>
      <p>Пожалуйста, введите имя и попробуйте снова.</p>
      <a href="/form">Вернуться к форме</a>
    `);
  }

  // Проверка на значение "admin"
  if (name.toLowerCase() === 'admin') {
    logger.warn('Admin access detected in form submission');
  }

  // Логирование успешной отправки формы
  logger.info(`Form submitted with name: ${name}`);
  res.send(`
    <h1>Спасибо, ${name}!</h1>
    <p>Ваша заявка была записана в лог.</p>
    <a href="/">Вернуться на главную</a>
  `);
});

// Страница для записи дебаг-лога
app.get('/debug', (req, res) => {
  logger.debug('Debug route accessed. This is a detailed debug log.');
  res.send(`
    <h1>Дебаг-лог записан!</h1>
    <p>Дебаг-лог был записан в консоль и файл.</p>
    <a href="/">Вернуться на главную</a>
  `);
});

// Обработка непредвиденных ошибок
app.use((err, req, res, next) => {
  logger.error(`Unhandled error: ${err.message}`);
  res.status(500).send(`
    <h1>Что-то пошло не так!</h1>
    <p>Необработанная ошибка была записана в лог.</p>
    <a href="/">Вернуться на главную</a>
  `);
});

// Запуск сервера
app.listen(PORT, () => {
  logger.info(`Server is running on http://localhost:${PORT}`);
});