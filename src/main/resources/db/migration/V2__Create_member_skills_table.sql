CREATE TABLE member_skills
(
    member_id BIGINT NOT NULL,
    skill_id  BIGINT NOT NULL,
    PRIMARY KEY (member_id, skill_id)
);