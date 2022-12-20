INSERT INTO USERS
( NAME, EMAIL)
VALUES ( 'Anton', 'agabov@gmail.com'),
       ('Dmitriy', 'dima@gmail.com'),
       ( 'Ivan', 'ivan@gmail.com'),
       ('Konstantin', 'kostya@gmail.com'),
       ( 'Petr', 'petya@gmail.com');

INSERT INTO ITEMS
( NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES('Аккумуляторная дрель', 'Аккумуляторная дрель + аккумулятор', true, 1),
      ('лампочка', 'светодиодная лампа новая', true, 3),
      ('тапочки', 'теплые, домашние', true, 4),
      ('полотенце', 'махровое, синее', true, 2),
      ('компьютер', 'пентиум 4', true, 3),
      ('игрушка мягкая', 'хаги-ваги', true, 4),
      ('кресло комьютерное', 'кожаное из икеи', true, 3),
      ('сотовый телефон', 'нокиа', true, 2),
      ( 'Отвертка','Аккумуляторная отвертка',true,1);
