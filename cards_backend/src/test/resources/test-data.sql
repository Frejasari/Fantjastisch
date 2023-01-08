-- create a clear state before every test.
truncate table public.categories;
truncate table public.cards;
truncate table public.links;
truncate table public.categories;
truncate table public.learning_box;
truncate table public.learning_systems;

INSERT INTO public.CATEGORIES (id, label, sub_category_ids)
VALUES
    -- 3b182412-0d6d-4857-843a-edfc1973d323	                            dbfa51dd-e8e9-4cc6-ae34-ce62e12ab2c2    fce013e8-0282-4106-8ff9-b05dba5ba550
    ('3b1824120d6d4857843aedfc1973d323', 'Technische Informatik', ARRAY['dbfa51dde8e94cc6ae34ce62e12ab2c2',
     'fce013e8028241068ff9b05dba5ba550']),
    -- 40ac4fcc-9702-4a87-b 0bd-bffe1f7f49f8                              c880b0a4-6106-4394-8e7d-521dae20b644
    ('40ac4fcc97024a87b0bdbffe1f7f49f8', 'Praktische Informatik', ARRAY['c880b0a4610643948e7d521dae20b644',
     'fce013e8028241068ff9b05dba5ba550']);
-- b1f5e79c-2f2b-4a48-bf93-5a2439f2301e
--     ('b1f5e79c2f2b4a48bf935a2439f2301e', 'Mathematik', ARRAY[]);
--     -- 3b182412-0d6d-4857-843a-edfc1973d323

INSERT INTO public.CARDS (id, question, answer, tag, categories)
VALUES ('12557dce-4dbf-4531-869a-fd9e20533b7e', 'Welche Verdrängungsstrategien gibt es?', 'FIFO, LRU, LFU', 'Wichtig',
        '3b1824120d6d4857843aedfc1973d323'),
       ('47da14f8-c6e1-48e4-9071-ed92f5694aff', 'Nenne zwei Cachearten.', 'Direct-Mapped und Voll-assoziativ', '',
        '3b1824120d6d4857843aedfc1973d323');
