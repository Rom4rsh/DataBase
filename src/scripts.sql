-- Получить всех студентов, возраст которых между 10 и 20 лет
SELECT * FROM student WHERE age BETWEEN 10 AND 20;

-- Получить всех студентов, но отобразить только их имена
SELECT name FROM student;

-- Получить всех студентов, у которых в имени есть буква 'О'
SELECT * FROM student WHERE name LIKE '%О%';

-- Получить всех студентов, у которых возраст меньше идентификатора
SELECT * FROM student WHERE age < id;

-- Получить всех студентов, упорядоченных по возрасту
SELECT * FROM student ORDER BY age;