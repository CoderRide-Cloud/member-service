CREATE TABLE IF NOT EXISTS members (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    bio TEXT,
    role_title VARCHAR(255),
    dev_stack VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255)
);
