CREATE TABLE conversations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT RANDOM_UUID(),
    user_uuid UUID NOT NULL,
    title VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_conversations_public_id UNIQUE (public_id)
);

CREATE INDEX idx_conversations_user_uuid ON conversations(user_uuid);
CREATE INDEX idx_conversations_created_at ON conversations(created_at DESC);
