const express = require('express');
const bodyParser = require('body-parser');
const sequelize = require('./util/database');
const User = require('./models/user');
const logger = require('./logger'); // Импорт логгера

const app = express();

// Настройка статических файлов
app.use(express.static('public'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// Логирование всех запросов
app.use((req, res, next) => {
  logger.info(`Incoming request: ${req.method} ${req.url}`);
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
  next();
});

// Главная страница
app.get('/', (req, res, next) => {
  logger.info('GET / endpoint hit');
  res.send(`
    <h1>Добро пожаловать!</h1>
    <a href="/users">Перейти к списку пользователей</a>
  `);
});

// Маршрут для отображения интерфейса пользователей
app.get('/users', (req, res, next) => {
  User.findAll()
    .then(users => {
      res.send(`
        <h1>Список пользователей</h1>
        <ul>
          ${users.map(user => `
            <li>
              ${user.name} - ${user.email}
              <form action="/users/${user.id}?_method=DELETE" method="POST" style="display:inline;">
                <button type="submit">Удалить</button>
              </form>
            </li>
          `).join('')}
        </ul>
        <form action="/users" method="POST">
          <input type="text" name="name" placeholder="Имя">
          <input type="email" name="email" placeholder="Email">
          <button type="submit">Добавить пользователя</button>
        </form>
        <br>
        <a href="/">Вернуться на главную</a>
      `);
    })
    .catch(err => {
      logger.error(`Error fetching users: ${err.message}`);
      res.status(500).send('Ошибка при получении пользователей');
    });
});

// Маршрут для добавления пользователя
app.post('/users', (req, res, next) => {
  const { name, email } = req.body;

  if (!name || !email) {
    logger.error('Error adding user: Fields "name" and "email" are required');
    return res.status(400).send('Поля "Имя" и "Email" обязательны для заполнения');
  }
  
  User.create({ name, email })
    .then(() => {
      logger.info(`User added: ${name} (${email})`);
      res.redirect('/users');
    })
    .catch(err => {
      logger.error(`Error adding user: ${err.message}`);
      res.status(500).send('Ошибка при добавлении пользователя');
    });
});

// Маршрут для удаления пользователя
app.post('/users/:userId', (req, res, next) => {
  const userId = req.params.userId;
  User.findByPk(userId)
    .then(user => {
      if (!user) {
        return res.status(404).send('Пользователь не найден');
      }
      return User.destroy({
        where: {
          id: userId
        }
      });
    })
    .then(() => {
      logger.info(`User deleted: ${userId}`);
      res.redirect('/users');
    })
    .catch(err => {
      logger.error(`Error deleting user: ${err.message}`);
      res.status(500).send('Ошибка при удалении пользователя');
    });
});

// Обработка ошибок
app.use((error, req, res, next) => {
  logger.error(`Error occurred: ${error.message}`);
  const status = error.statusCode || 500;
  const message = error.message;
  res.status(status).json({ message: message });
});

// Синхронизация базы данных и запуск сервера
sequelize
  .sync()
  .then(result => {
    logger.info('Database connected successfully');
    app.listen(3000, () => {
      logger.info('Server started on port 3000');
    });
  })
  .catch(err => logger.error(`Database connection error: ${err.message}`));