INSERT INTO USERS (VK_ID, ROLE, TEL, MONEY)
VALUES (123, 'USER', '+7 (900) 123-45-67', 0),
       (111, 'ADMIN', '+7 (921) 123-45-67', 123),
       (115, 'USER2', '+7 (906) 123-45-67', 2);

INSERT INTO PAY (USER_ID, MONEY, PAY_TIME)
VALUES (123, 5, 12345678),
       (115, 3, 87656321),
       (115, 4, 87676721),
       (123, 1, 87654321);

INSERT INTO INVITE (USER_ID, INVITED_ID, INVITE_TIME)
VALUES (123, 1234, 12345677),
       (115, 4326, 76543211),
       (115, 4327, 76563211),
       (115, 4328, 76583211),
       (111, 4321, 76543211);