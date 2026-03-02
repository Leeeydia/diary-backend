-- ============================================
-- Emotion Diary - DB Schema (CODE-BASED)
-- ============================================

CREATE DATABASE IF NOT EXISTS diary
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE diary;

-- 기존 테이블이 있다면(개발환경에서만)
-- DROP TABLE IF EXISTS ai_reply;
-- DROP TABLE IF EXISTS board;
-- DROP TABLE IF EXISTS diary;
-- DROP TABLE IF EXISTS refresh_token;
-- DROP TABLE IF EXISTS member;

-- --------------------------------------------
-- 1. member
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS member (
                                      id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username          VARCHAR(50)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    email             VARCHAR(100) NOT NULL,
    nickname          VARCHAR(50)  NOT NULL,
    role              VARCHAR(20)  NOT NULL,
    reply_mode        ENUM('PARENT','TEACHER') NULL,
    profile_image_url VARCHAR(500) NULL,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NULL,
    UNIQUE KEY uq_member_username (username),
    UNIQUE KEY uq_member_email (email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 2. refresh_token
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS refresh_token (
                                             id         BIGINT       AUTO_INCREMENT PRIMARY KEY,
                                             member_id  BIGINT       NOT NULL,
                                             token      VARCHAR(512) NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_refresh_token_member_id (member_id),
    KEY idx_refresh_token_token (token),
    CONSTRAINT fk_refresh_token_member
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 3. diary
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS diary (
                                     id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     member_id  BIGINT NOT NULL,
                                     content    TEXT NOT NULL,
                                     emotion    ENUM('HAPPY','SAD','ANGRY','TIRED','CALM','EXCITED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    KEY idx_diary_member_id (member_id),
    CONSTRAINT fk_diary_member
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 4. board
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS board (
                                     id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     member_id  BIGINT NOT NULL,
                                     title      VARCHAR(255) NOT NULL,
    content    TEXT NOT NULL,
    emotion    ENUM('HAPPY','SAD','ANGRY','TIRED','CALM','EXCITED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_board_member
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------
-- 5. ai_reply
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS ai_reply (
                                        id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        diary_id      BIGINT NOT NULL,
                                        reply_type    VARCHAR(20) NOT NULL,
    reply_content TEXT NOT NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_reply_diary
    FOREIGN KEY (diary_id) REFERENCES diary(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- (선택) 서비스에서 1:1을 강제하지만 DB에서도 막고 싶으면 아래 추가
-- CREATE UNIQUE INDEX uq_ai_reply_diary_id ON ai_reply(diary_id);