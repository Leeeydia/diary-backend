-- ============================================
-- Emotion Diary - Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS diary
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE diary;

-- --------------------------------------------
-- 1. users
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    nickname    VARCHAR(50)     NOT NULL,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 2. diaries
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS diaries (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    title           VARCHAR(200)    NOT NULL,
    content         TEXT            NOT NULL,
    voice_url       VARCHAR(500)    NULL,
    emotion         VARCHAR(50)     NULL,
    emotion_score   INT             NULL,
    written_date    DATE            NOT NULL,
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_diary_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_diary_user_id ON diaries(user_id);
CREATE INDEX idx_diary_written_date ON diaries(written_date);

-- --------------------------------------------
-- 3. emotions (detailed emotion breakdown per diary)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS emotions (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    diary_id    BIGINT          NOT NULL,
    emotion_tag VARCHAR(50)     NOT NULL,
    score       INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_emotion_diary FOREIGN KEY (diary_id) REFERENCES diaries(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_emotion_diary_id ON emotions(diary_id);
