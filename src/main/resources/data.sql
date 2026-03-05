INSERT INTO users (id, email, password_hash, created_at)
VALUES (
    gen_random_uuid(),
    'test@test.com',
    '$2a$10$6qfFUmKY8pg3D5b3lr3RKurRoRVbiJ4p.dfkPYaiQ7K5yTsSKjXUW',
    NOW()
)
ON CONFLICT (email) DO NOTHING;
