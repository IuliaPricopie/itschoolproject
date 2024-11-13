INSERT INTO owner (id, mail, username, password, role, age)
VALUES (1, 'client1@example.com', 'client1', '$2y$10$jT3wiSPu1uF3pYHGkKLQruFXF/S0Jq85Jip198f6hjOG0fQ.FBYZG', 'OWNER', 30);

INSERT INTO owner (id, mail, username, password, role, age)
VALUES (2, 'client2@example.com', 'client2', '$2y$10$ON4ubnAzkSNC1YGqE0dUbebx4ecvQrNi9zBkky0T/Ho1tFFB7IOOu', 'OWNER', 25);

INSERT INTO librarian (id, mail, username, password, role, hiring_date)
VALUES (3, 'librarian@example.com', 'librarian', '$2y$10$UIFU.id9Z8pqGFrpJ.xCRu5ql8Cp6eqc/3Tho8GmltGmA1Us5KSpy', 'LIBRARIAN', '2023-01-15');
--//client1, client2, parola

INSERT INTO Author (name, birth_date, death_date) VALUES
('MIHAI EMINESCU', '1850-01-15', '1889-06-15'),
('ION CREANGĂ', '1837-03-01', '1889-12-31'),
('MARIN PREDA', '1922-08-05', '1980-05-16'),
('IOAN SLAVICI', '1848-01-18', '1925-08-17'),
('MIHAIL SADOVEANU', '1880-11-05', '1961-10-19');

INSERT INTO Book (name, page, price, is_borrowed, author_id) VALUES
('LUCEAFĂRUL', 50, 20, false, 1),
('AMINTIRI DIN COPILĂRIE', 250, 40, false, 2),
('PĂCALĂ', 90, 20, false, 2),
('MOROMEȚII', 600, 50, false, 3),
('CEL MAI IUBIT DINTRE PĂMÂNTENI', 720, 60, false, 3),
('MOARA CU NOROC', 160, 25, false, 4),
('POPA TANDA', 70, 18, false, 4),
('BALTAGUL', 300, 40, false, 5),
('HANUL ANCUȚEI', 220, 36, false, 5);