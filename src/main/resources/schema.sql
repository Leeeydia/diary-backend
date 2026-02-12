-- ============================================
-- Emotion Diary - Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS diary
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE diary;

-- --------------------------------------------
-- 1. member
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS member (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    role        VARCHAR(20)     DEFAULT 'USER',
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 2. refresh_token
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS refresh_token (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT          NOT NULL,
    token       VARCHAR(500)    NOT NULL,
    expires_at  DATETIME        NOT NULL,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_token_member FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE UNIQUE INDEX idx_refresh_token_member ON refresh_token(member_id);
CREATE INDEX idx_refresh_token_token ON refresh_token(token);

-- --------------------------------------------
-- 3. diary
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS diary (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT          NOT NULL,
    content     TEXT            NOT NULL,
    emotion     VARCHAR(50)     NULL,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted  BOOLEAN         DEFAULT FALSE,
    CONSTRAINT fk_diary_member FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_diary_member_id ON diary(member_id);

-- --------------------------------------------
-- 4. ai_reply
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS ai_reply (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    diary_id        BIGINT          NOT NULL,
    reply_type      VARCHAR(20)     NOT NULL,
    reply_content   TEXT            NOT NULL,
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_reply_diary FOREIGN KEY (diary_id) REFERENCES diary(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_ai_reply_diary_id ON ai_reply(diary_id);
