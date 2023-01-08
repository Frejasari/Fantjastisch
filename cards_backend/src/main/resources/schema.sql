CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.cards
(
    id       UUID NOT NULL PRIMARY KEY,
    question TEXT,
    answer   TEXT,
    tag      TEXT,
    categories UUID ARRAY
);

CREATE TABLE IF NOT EXISTS public.links
(
    name    TEXT,
    source  UUID,
    target  UUID,
    PRIMARY KEY (name, source)
    );

CREATE TABLE IF NOT EXISTS public.categories
(
    id        UUID PRIMARY KEY,
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
    id UUID PRIMARY KEY,
    label TEXT,
    num_boxes INTEGER
);