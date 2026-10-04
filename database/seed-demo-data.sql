-- PostgreSQL demo dataset: 300 people, 720 payments, 240 programs and exercises.
-- Run after the services have created their schemas and tables.
BEGIN;
SET LOCAL TIME ZONE 'UTC';

-- Convert legacy timestamp audit/payment columns to calendar dates.
ALTER TABLE persons.persons
    ALTER COLUMN created_at TYPE date USING created_at::date,
    ALTER COLUMN updated_at TYPE date USING updated_at::date;
ALTER TABLE payments.payments
    ALTER COLUMN paid_at TYPE date USING paid_at::date,
    ALTER COLUMN created_at TYPE date USING created_at::date,
    ALTER COLUMN updated_at TYPE date USING updated_at::date;
ALTER TABLE trainingprograms.training_programs
    ALTER COLUMN created_at TYPE date USING created_at::date,
    ALTER COLUMN updated_at TYPE date USING updated_at::date;

DELETE FROM payments.payments WHERE description LIKE 'DEMO:%';
DELETE FROM trainingprograms.training_program_exercises
WHERE training_program_id IN (
    SELECT id FROM trainingprograms.training_programs WHERE name LIKE 'DEMO:%'
);
DELETE FROM trainingprograms.training_programs WHERE name LIKE 'DEMO:%';

WITH generated_people AS (
    SELECT
        n,
        CASE
            WHEN n <= 240 THEN 'ALUNO'
            WHEN n <= 290 THEN 'PERSONAL'
            ELSE 'ADMIN'
        END AS person_type,
        CASE WHEN n % 2 = 0 THEN 'M' ELSE 'F' END AS gender,
        CASE WHEN n % 2 = 0 THEN 'Lucas' ELSE 'Mariana' END AS first_name,
        CASE WHEN n % 4 < 2 THEN 'Silva' ELSE 'Oliveira' END AS last_name
    FROM generate_series(1, 300) AS series(n)
),
seeded_people AS (
    INSERT INTO persons.persons (
        username, password, name, birth_date, gender, person_type,
        internal_personal, cref, cpf, email, note, height, weight,
        body_fat_percentage, created_at, updated_at
    )
    SELECT
        'demo.' || lower(person_type) || '.' || lpad(n::text, 4, '0'),
        'Demo@12345',
        first_name || ' ' || last_name || ' ' || lpad(n::text, 4, '0'),
        current_date - ((18 * 365 + (n * 37) % (52 * 365))::integer),
        gender,
        person_type,
        CASE WHEN person_type = 'PERSONAL' THEN n % 2 = 0 ELSE false END,
        CASE WHEN person_type = 'PERSONAL' THEN 'D' || lpad(n::text, 8, '0') END,
        lpad((10000000000::bigint + n)::text, 11, '0'),
        'demo.' || lpad(n::text, 4, '0') || '@example.com',
        CASE n % 5
            WHEN 0 THEN 'DEMO: foco em condicionamento'
            WHEN 1 THEN 'DEMO: objetivo de hipertrofia'
            WHEN 2 THEN 'DEMO: retorno gradual aos treinos'
            WHEN 3 THEN 'DEMO: acompanhamento de mobilidade'
            ELSE 'DEMO: rotina de atividade física'
        END,
        round((1.50 + (n % 50) * 0.01)::numeric, 2)::double precision,
        round((48 + (n * 7 % 65) * 0.5)::numeric, 1)::double precision,
        CASE WHEN n % 6 = 0 THEN NULL
             ELSE round((12 + (n * 13 % 220) * 0.1)::numeric, 1)::double precision
        END,
        current_date - (n % 180),
        current_date - (n % 30)
    FROM generated_people
    ON CONFLICT (username) DO UPDATE SET
        password = EXCLUDED.password,
        name = EXCLUDED.name,
        birth_date = EXCLUDED.birth_date,
        gender = EXCLUDED.gender,
        person_type = EXCLUDED.person_type,
        internal_personal = EXCLUDED.internal_personal,
        cref = EXCLUDED.cref,
        cpf = EXCLUDED.cpf,
        email = EXCLUDED.email,
        note = EXCLUDED.note,
        height = EXCLUDED.height,
        weight = EXCLUDED.weight,
        body_fat_percentage = EXCLUDED.body_fat_percentage,
        updated_at = EXCLUDED.updated_at
    RETURNING id, username
),
cleared_conditions AS (
    DELETE FROM persons.person_medical_conditions
    WHERE person_id IN (SELECT id FROM seeded_people)
    RETURNING person_id
)
INSERT INTO persons.person_medical_conditions (person_id, medical_condition)
SELECT
    person.id,
    condition.medical_condition
FROM seeded_people AS person
JOIN LATERAL unnest(
    CASE
        WHEN right(person.username, 4)::integer % 7 = 0
            THEN ARRAY['Asma', 'Alergia sazonal']::text[]
        WHEN right(person.username, 4)::integer % 5 = 0
            THEN ARRAY['Hipertensão controlada']::text[]
        WHEN right(person.username, 4)::integer % 3 = 0
            THEN ARRAY['Lesão prévia no joelho', 'Acompanhamento médico']::text[]
        ELSE ARRAY[]::text[]
    END
) AS condition(medical_condition) ON true;

WITH demo_students AS (
    SELECT
        id,
        right(username, 4)::integer AS n
    FROM persons.persons
    WHERE username LIKE 'demo.aluno.%'
)
INSERT INTO payments.payments (
    person_id, amount, method, installment_count, due_date,
    paid_at, status, description, created_at, updated_at
)
SELECT
    student.id,
    (79 + (student.n * installment.number % 9) * 15)::numeric(12, 2),
    (ARRAY['CREDIT', 'DEBIT', 'CASH', 'PIX'])[
        1 + (student.n + installment.number) % 4
    ],
    CASE WHEN installment.number = 1 THEN 1
         ELSE 1 + (student.n + installment.number) % 6
    END,
    current_date + ((student.n % 15) - (installment.number * 20)),
    CASE WHEN (student.n + installment.number) % 10 < 6
         THEN current_date - ((student.n + installment.number) % 45)
         ELSE NULL
    END,
    CASE
        WHEN (student.n + installment.number) % 10 < 6 THEN 'PAID'
        WHEN (student.n + installment.number) % 10 = 6 THEN 'OVERDUE'
        WHEN (student.n + installment.number) % 10 = 7 THEN 'CANCELLED'
        ELSE 'PENDING'
    END,
    'DEMO: mensalidade ' || installment.number || ' - aluno ' || lpad(student.n::text, 4, '0'),
    current_date - ((student.n + installment.number) % 180),
    current_date - ((student.n + installment.number) % 30)
FROM demo_students AS student
CROSS JOIN generate_series(1, 3) AS installment(number);

WITH demo_students AS (
    SELECT
        id,
        right(username, 4)::integer AS n
    FROM persons.persons
    WHERE username LIKE 'demo.aluno.%'
),
demo_trainers AS (
    SELECT
        id,
        row_number() OVER (ORDER BY id)::integer AS n
    FROM persons.persons
    WHERE username LIKE 'demo.personal.%'
),
seeded_programs AS (
    INSERT INTO trainingprograms.training_programs (
        student_id, trainer_id, name, created_at, updated_at
    )
    SELECT
        student.id,
        trainer.id,
        'DEMO: Treino ' || lpad(student.n::text, 4, '0') ||
            CASE WHEN student.n % 2 = 0 THEN ' - Hipertrofia' ELSE ' - Condicionamento' END,
        current_date - (student.n % 180),
        current_date - (student.n % 30)
    FROM demo_students AS student
    JOIN demo_trainers AS trainer
        ON trainer.n = 1 + (student.n - 1) % 50
    RETURNING id, student_id
)
INSERT INTO trainingprograms.training_program_exercises (
    training_program_id, exercise_order, exercise_name, sets_count,
    repetitions, rest_seconds, notes
)
SELECT
    program.id,
    exercise.number - 1,
    exercise.name,
    3 + (student.n + exercise.number) % 3,
    CASE WHEN (student.n + exercise.number) % 2 = 0 THEN 10 ELSE 12 END,
    30 + ((student.n + exercise.number) % 4) * 15,
    CASE
        WHEN (student.n + exercise.number) % 4 = 0 THEN 'DEMO: priorizar a técnica'
        WHEN (student.n + exercise.number) % 4 = 1 THEN 'DEMO: aumentar carga gradualmente'
        WHEN (student.n + exercise.number) % 4 = 2 THEN 'DEMO: manter amplitude confortável'
        ELSE 'DEMO: controlar a respiração'
    END
FROM seeded_programs AS program
JOIN demo_students AS student ON student.id = program.student_id
CROSS JOIN LATERAL unnest(
    ARRAY[
        'Agachamento livre',
        'Supino reto',
        'Remada baixa',
        'Levantamento terra romeno',
        'Desenvolvimento com halteres',
        'Puxada frontal',
        'Prancha'
    ]::text[]
) WITH ORDINALITY AS exercise(name, number)
WHERE exercise.number <= 4 + student.n % 4;

COMMIT;
