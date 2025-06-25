-- Migration para crear VIEW de base de datos - REQUISITO TÉCNICO
-- Implementa el requisito de tener una Vista de base de datos

-- Crear VIEW que combine información de películas y géneros
CREATE OR REPLACE VIEW movie_genre_stats AS
SELECT 
    m.id as movie_id,
    m.title,
    m.release_date,
    m.vote_average,
    m.popularity,
    g.id as genre_id,
    g.name as genre_name,
    g.description as genre_description,
    -- Estadísticas calculadas
    CASE 
        WHEN m.vote_average >= 8.0 THEN 'Excelente'
        WHEN m.vote_average >= 7.0 THEN 'Muy Buena'
        WHEN m.vote_average >= 6.0 THEN 'Buena'
        WHEN m.vote_average >= 5.0 THEN 'Regular'
        ELSE 'Mala'
    END as rating_category,
    -- Categoría de popularidad
    CASE 
        WHEN m.popularity >= 100 THEN 'Muy Popular'
        WHEN m.popularity >= 50 THEN 'Popular'
        WHEN m.popularity >= 20 THEN 'Moderada'
        ELSE 'Baja'
    END as popularity_category,
    -- Información temporal
    EXTRACT(YEAR FROM m.release_date) as release_year,
    EXTRACT(MONTH FROM m.release_date) as release_month,
    m.created_at,
    m.updated_at
FROM movies m
LEFT JOIN genres g ON m.genre_id = g.id
WHERE m.release_date IS NOT NULL
ORDER BY m.popularity DESC, m.vote_average DESC; 