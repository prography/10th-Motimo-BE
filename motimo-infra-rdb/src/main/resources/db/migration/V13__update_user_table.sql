ALTER TABLE users
    ADD COLUMN bio VARCHAR(255);

ALTER TABLE users
    RENAME COLUMN profile_image_url TO profile_image_path;