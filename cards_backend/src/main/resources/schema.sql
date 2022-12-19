CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.cards
(
    id       UUID default random_uuid(),
    question TEXT,
    answer   TEXT,
    tag      TEXT
);
CREATE TABLE IF NOT EXISTS public.categories
(
    id        UUID default random_uuid() PRIMARY KEY,
    label     TEXT,
    sub_category_ids UUID ARRAY
);
CREATE TABLE IF NOT EXISTS public.learning_box
(
    learning_system_id UUID,
    box_no INTEGER,
    label TEXT,
    PRIMARY KEY (box_no, learning_system_id)
);
CREATE TABLE IF NOT EXISTS public.learning_systems
(
    id UUID default random_uuid(),
    label TEXT,
    num_boxes INTEGER
);