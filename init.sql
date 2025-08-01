\c portfoliomanager;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
    RAISE NOTICE 'Banco de dados portfoliomanager inicializado com sucesso!';
    RAISE NOTICE 'As tabelas serão criadas automaticamente pelo Spring Boot.';
END $$; 