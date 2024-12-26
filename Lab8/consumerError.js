const { Kafka } = require('kafkajs');
const logger = require('./logger'); 

const kafka = new Kafka({
  clientId: 'my-app-multi-topic-consumer',
  brokers: ['localhost:9092']
});

const consumer = kafka.consumer({ groupId: 'multi-topic-consumer-group' });

// Хранилище для уникальных сообщений
const processedMessages = new Set();

const start = async () => {
  try {
    // Подключаем консюмер
    await consumer.connect();
    logger.info('Консюмер подключён к Kafka');

    // Подписываемся на оба топика
    await consumer.subscribe({ topic: 'dead-letter-queue', fromBeginning: true });
    await consumer.subscribe({ topic: 'test-topic-upper', fromBeginning: true });
    logger.info('Консюмер подписан на топики dead-letter-queue и test-topic-upper');

    // Обработка сообщений
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        try {
          const messageValue = message.value.toString();

          // Обработка сообщений из dead-letter-queue
          if (topic === 'dead-letter-queue') {
            const { message: originalMessage, consumerId } = JSON.parse(messageValue);

            // Проверяем, было ли сообщение уже обработано
            if (processedMessages.has(originalMessage)) {
              logger.error(`Сообщение из DLQ уже обработано: ${originalMessage} (Консюмер: ${consumerId})`);
              return; // Пропускаем дубликат
            }

            // Добавляем сообщение в хранилище
            processedMessages.add(originalMessage);

            logger.info(`Сообщение из ${topic}: ${originalMessage} (Консюмер: ${consumerId})`);
          }

          // Обработка сообщений из test-topic-upper
          else if (topic === 'test-topic-upper') {
            logger.info(`Сообщение из ${topic}: ${messageValue}`);

            // Здесь можно добавить логику обработки для test-topic-upper
          }
        } catch (error) {
          logger.error(`Ошибка при обработке сообщения из ${topic}: ${error.message}`);
        }
      },
    });
  } catch (error) {
    logger.error(`Ошибка при запуске консюмера: ${error.message}`);
  }
};

start();