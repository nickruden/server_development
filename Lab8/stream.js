const { Kafka } = require('kafkajs');
const logger = require('./logger'); // Импортируем логгер

const kafka = new Kafka({
  clientId: 'my-app',
  brokers: ['localhost:9092']
});

const consumer = kafka.consumer({ groupId: 'test-group' });
const producer = kafka.producer();

const start = async () => {
  try {
    // Подключаем консюмер и продюсер
    await consumer.connect();
    await producer.connect();
    logger.info('Поток запущен: подключение к Kafka успешно');

    // Подписываемся на топик
    await consumer.subscribe({ topic: 'test-topic', fromBeginning: true });
    logger.info('Поток подписан на топик test-topic');

    // Обработка сообщений
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        try {
          const messageValue = message.value.toString();
          logger.info(`Поток: сообщение получено из ${topic}, партиция ${partition}: ${messageValue}`);

          // Пример обработки сообщения (например, преобразование в верхний регистр)
          const transformedMessage = messageValue.toUpperCase();
          logger.info(`Поток: сообщение преобразовано: ${transformedMessage}`);

          // Отправка преобразованного сообщения в другую тему
          await producer.send({
            topic: 'test-topic-upper',
            messages: [{ value: transformedMessage }],
          });
          logger.info(`Поток: сообщение отправлено в test-topic-upper: ${transformedMessage}`);
        } catch (error) {
          logger.error(`Поток: ошибка при обработке сообщения: ${error.message}`);
        }
      },
    });
  } catch (error) {
    logger.error(`Поток: ошибка при запуске: ${error.message}`);
  }
};

start();