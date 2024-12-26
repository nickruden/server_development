const { Kafka, Partitioners } = require('kafkajs');
const express = require('express');
const bodyParser = require('body-parser');
const { faker } = require('@faker-js/faker');
const logger = require('./logger');

const app = express();
app.use(bodyParser.json());

const kafka = new Kafka({
  clientId: 'my-app',
  brokers: ['localhost:9092']
});

const producer = kafka.producer({
  createPartitioner: Partitioners.LegacyPartitioner
});

// Функция для генерации случайной строки
const generateRandomString = () => {
  return faker.lorem.sentence(); // Используем faker для генерации случайного предложения
};

// Функция для отправки случайного сообщения в Kafka
const sendRandomMessage = async () => {
  const randomMessage = generateRandomString(); // Генерируем случайное сообщение
  try {
    await producer.send({
      topic: 'test-topic',
      messages: [{ value: `automeassage: ${randomMessage}` }],
    });
    logger.info(`Авто сообщение отправлено: ${message}`);
  } catch (error) {
    console.error('Error sending message:', error);
    logger.error('Ошибка при отправке авто сообщения:', error);
  }
};

// Запускаем периодическую отправку сообщений
const startPeriodicSending = (interval = 99999) => {
  setInterval(sendRandomMessage, interval);
};

// Эндпоинт для отправки кастомного сообщения
app.post('/send', async (req, res) => {
  const { message } = req.body;
  try {
    await producer.send({
      topic: 'test-topic',
      messages: [{ value: message }],
    });
    logger.info(`Сообщение пользователя отправлено: ${message}`);
    res.send('Message sent');
  } catch (error) {
    console.error('Error sending message:', error);
    logger.error('Ошибка при отправке сообщения пользователя:', error);
    res.status(500).send('Error sending message');
  }
});

const start = async () => {
  await producer.connect();
  app.listen(3000, () => {
    console.log('Producer running on port 3000');
    logger.info('Producer running on port 3000');
  });
//   startPeriodicSending();
};

start();