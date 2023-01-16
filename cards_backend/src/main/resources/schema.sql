CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.cards
(
    id       UUID PRIMARY KEY,
    question TEXT NOT NULL,
    answer   TEXT NOT NULL,
    tag      TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS public.links
(
    id      UUID,
    name    TEXT,
    source  UUID,
    target  UUID,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.categories
(
    id        UUID PRIMARY KEY,
    label     TEXT NOT NULL,
    sub_category_ids UUID ARRAY
);

CREATE TABLE IF NOT EXISTS public.learning_box
(
    learning_system_id UUID,
    box_no INTEGER NOT NULL,
    label TEXT NOT NULL,
    PRIMARY KEY (box_no, learning_system_id)
    );

CREATE TABLE IF NOT EXISTS public.learning_systems
(
    id UUID PRIMARY KEY,
    label TEXT NOT NULL,
    box_labels TEXT ARRAY
);

CREATE TABLE IF NOT EXISTS public.categories_to_cards
(
    category_id UUID,
    card_id UUID,
    PRIMARY KEY (category_ID, card_ID)
--     foreign key (category_id) references public.categories(id),
--     foreign key (card_id) references public.cards(id)
    );