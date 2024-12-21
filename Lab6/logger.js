const { createLogger, format, transports } = require('winston');
const colors = require('colors');

const logColors = {
  error: 'red',
  warn: 'yellow',
  info: 'green',
  http: 'cyan',
  debug: 'blue',
};

colors.setTheme(logColors);

const logFormat = format.combine(
  format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
  format.printf(({ level, message, timestamp }) => {
    const colorizedLevel = level.toUpperCase()[logColors[level] || 'white'];
    return `${timestamp} [${colorizedLevel}] ${message}`;
  })
);

const logger = createLogger({
  level: 'debug',
  format: logFormat,
  transports: [
    new transports.Console({
      format: format.combine(
        format.printf(({ level, message, timestamp }) =>
          `${timestamp} ${level.toUpperCase()[logColors[level] || 'white']}: ${message}`
        )
      ),
    }),
    new transports.File({ filename: 'logs/app.log' }),
  ],
});

module.exports = logger;