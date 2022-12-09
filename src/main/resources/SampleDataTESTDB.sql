INSERT INTO "users"
( "name", "email")
VALUES ( 'Anton', 'agabov@gmail.com'),
       ('Dmitriy', 'dima@gmail.com'),
       ( 'Ivan', 'ivan@gmail.com'),
       ('Konstantin', 'kostya@gmail.com'),
       ( 'Petr', 'petya@gmail.com');

INSERT INTO "items"
( "name", "description", "is_available", "owner_id")
VALUES('Аккумуляторная дрель', 'Аккумуляторная дрель + аккумулятор', true, 1),
      ( 'Отвертка','Аккумуляторная отвертка',true,1);