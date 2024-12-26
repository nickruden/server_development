const { Kafka } = require('kafkajs');
const logger = require('./logger');

// Получаем номер консюмера из аргументов командной строки
const consumerId = process.argv[2] || '1'; // По умолчанию 1, если номер не указан

// Создаём логгер с метаданными для текущего консюмера
const consumerLogger = logger.withConsumer(consumerId);

const kafka = new Kafka({
    clientId: `my-app-consumer-${consumerId}`,
    brokers: ['localhost:9092']
});

const consumer = kafka.consumer({ groupId: 'test-group-' + process.pid });
const producer = kafka.producer(); // Продюсер для отправки в DLQ

// Функция для отправки сообщения в Dead Letter Queue
const sendToDLQ = async (message, consumerId) => {
    try {
      await producer.send({
        topic: 'dead-letter-queue',
        messages: [{
          value: JSON.stringify({
            message, // Само сообщение
            consumerId // Идентификатор консюмера
          })
        }],
      });
      consumerLogger.warn(`Сообщение отправлено в DLQ: ${message} (Консюмер: ${consumerId})`);
    } catch (error) {
      consumerLogger.error('Ошибка при отправке сообщения в DLQ:', error);
    }
  };

// Переменные для статистики
let messageCount = 0; // Счётчик сообщений
const messagesStorage = []; // Массив для хранения сообщений

// Функция для фильтрации сообщений
const filterMessages = (message) => {
    return message.includes('automeassage'); // Фильтруем сообщения, содержащие слово "automeassage"
};

// Функция для обработки сообщений
const processMessage = (message) => {
    // Увеличиваем счётчик сообщений
    messageCount++;
    consumerLogger.info(`Сообщение №${messageCount}: ${message}`);

    // Фильтрация сообщений
    if (filterMessages(message)) {
        consumerLogger.info(`Автоматическое сообщение`);
    } else {
        consumerLogger.info(`Сообщение от пользователя`);
    }

    // Сохраняем сообщение в массив
    messagesStorage.push(message);

    // Выводим текущую статистику
    consumerLogger.info(`Всего сообщений получено: ${messageCount}`);
    consumerLogger.info(`Всего авто сообщений: ${messagesStorage.filter(filterMessages).length} \n`);
};

const start = async () => {
    try {
        await consumer.connect();
        await producer.connect(); // Подключаем продюсер для DLQ
        consumerLogger.info('Консюмер подключён к Kafka');

        await consumer.subscribe({ topic: 'test-topic', fromBeginning: true });
        consumerLogger.info('Подписан на топик test-topic');

        await consumer.run({
            eachMessage: async ({ topic, partition, message }) => {
                try {
                    const messageValue = message.value.toString();
                    console.group(`Сообщение получено из ${topic}: ${messageValue}`);
                    consumerLogger.info(`Сообщение получено из ${topic}, партиция ${partition}: ${messageValue}`);
                    processMessage(messageValue);

                    // Пример обработки сообщения (может выбросить ошибку)
                    if (messageValue.includes('error')) {
                        throw new Error('Имитация ошибки обработки');
                    }

                    console.groupEnd();
                    consumerLogger.info(`Сообщение успешно обработано: ${messageValue}`);
                } catch (error) {
                    consumerLogger.error(`Ошибка при обработке сообщения: ${error.message}`);
                    // Отправляем сообщение в Dead Letter Queue
                    await sendToDLQ(message.value.toString(), consumerId); // Передаём consumerId
                }
            }
        });
    } catch (error) {
        consumerLogger.error('Ошибка при запуске консюмера:', error);
    }
};

start();