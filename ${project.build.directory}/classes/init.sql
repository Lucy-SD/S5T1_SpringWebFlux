CREATE DATABASE IF NOT EXISTS blackjack;

USE blackjack;

CREATE TABLE IF NOT EXISTS player (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    games_won INT DEFAULT 0,
    games_lost INT DEFAULT 0,
    games_pushed INT DEFAULT 0
);