FROM node:14

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем зависимости
COPY package*.json ./
RUN npm install

# Копируем скрипт ожидания
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Копируем исходный код приложения
COPY . .

# Экспонируем порт
EXPOSE 3000

# Запуск приложения с логами
CMD ["sh", "-c", "node index.js >> ./logs/app.log 2>&1"]