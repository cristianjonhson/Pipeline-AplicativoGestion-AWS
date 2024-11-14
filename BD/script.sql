-- Crear la base de datos
CREATE DATABASE gestion_eventos;

-- Conectar a la base de datos creada
\c gestion_eventos;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,                           -- Identificador único del usuario
    nombre VARCHAR(100) NOT NULL,                    -- Nombre del usuario
    apellido VARCHAR(100) NOT NULL,                  -- Apellido del usuario
    email VARCHAR(100) NOT NULL UNIQUE,              -- Correo electrónico único
    contrasena VARCHAR(100) NOT NULL,                -- Contraseña del usuario
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de registro automática
    rol VARCHAR(50) DEFAULT 'usuario'                -- Rol del usuario, por defecto 'usuario'
);

-- Tabla de categorías de eventos
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,                           -- Identificador único de la categoría
    nombre VARCHAR(100) NOT NULL UNIQUE              -- Nombre único de la categoría
);

-- Tabla de ciudades
CREATE TABLE ciudades (
    id SERIAL PRIMARY KEY,                           -- Identificador único de la ciudad
    nombre VARCHAR(100) NOT NULL UNIQUE              -- Nombre único de la ciudad
);

-- Tabla de eventos
CREATE TABLE eventos (
    id SERIAL PRIMARY KEY,                           -- Identificador único del evento
    titulo VARCHAR(100) NOT NULL,                    -- Título del evento
    descripcion TEXT,                                -- Descripción del evento
    fecha_inicio TIMESTAMP NOT NULL,                 -- Fecha de inicio del evento
    fecha_fin TIMESTAMP NOT NULL,                    -- Fecha de finalización del evento
    ubicacion VARCHAR(255),                          -- Ubicación del evento
    organizador_id INT NOT NULL REFERENCES usuarios(id), -- ID del organizador (referencia a la tabla de usuarios)
    categoria_id INT NOT NULL REFERENCES categorias(id), -- ID de la categoría (referencia a la tabla de categorías)
    ciudad_id INT NOT NULL REFERENCES ciudades(id),  -- ID de la ciudad (referencia a la tabla de ciudades)
    valor DECIMAL(10, 2),                            -- Precio del evento
    imagen_html TEXT,                                -- Imagen en formato HTML
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha de creación automática
);

-- Tabla de inscripciones a los eventos
CREATE TABLE inscripciones (
    id SERIAL PRIMARY KEY,                           -- Identificador único de la inscripción
    usuario_id INT NOT NULL REFERENCES usuarios(id), -- ID del usuario inscrito (referencia a la tabla de usuarios)
    evento_id INT NOT NULL REFERENCES eventos(id),   -- ID del evento (referencia a la tabla de eventos)
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de inscripción automática
    UNIQUE(usuario_id, evento_id)                    -- Restringir que un usuario se inscriba solo una vez a un evento
);

-- Índices para mejorar el rendimiento de las consultas
CREATE INDEX idx_eventos_fecha_inicio ON eventos(fecha_inicio); -- Índice para la columna de fecha de inicio
CREATE INDEX idx_eventos_fecha_fin ON eventos(fecha_fin);       -- Índice para la columna de fecha de fin
CREATE INDEX idx_eventos_categoria_id ON eventos(categoria_id); -- Índice para la columna de categoría
CREATE INDEX idx_eventos_ciudad_id ON eventos(ciudad_id);       -- Índice para la columna de ciudad

-- Insertar datos de prueba en la tabla de usuarios
INSERT INTO usuarios (nombre, apellido, email, contrasena, rol)
VALUES 
('Admin', 'Admin', 'admin@eventos.com', 'admin123', 'admin'), -- Usuario administrador
('Juan', 'Perez', 'juan.perez@example.com', 'password123', 'ROLE_usuario'), -- Usuario regular
('Maria', 'Garcia', 'maria.garcia@example.com', 'password123', 'usuario'); -- Otro usuario regular

-- Insertar datos de prueba en la tabla de categorías
INSERT INTO categorias (nombre)
VALUES 
('Verano Familiar'),  -- Categoría para eventos familiares
('Programación'),     -- Categoría para eventos de programación
('Música'),           -- Categoría para eventos de música
('Deportes');         -- Categoría para eventos deportivos

-- Insertar datos de prueba en la tabla de ciudades
INSERT INTO ciudades (nombre)
VALUES 
('Playa Central'),        -- Ciudad ficticia llamada Playa Central
('Parque de la Ciudad'),  -- Ciudad ficticia llamada Parque de la Ciudad
('Sala de Conferencias'), -- Ciudad ficticia llamada Sala de Conferencias
('Polideportivo Municipal'); -- Ciudad ficticia llamada Polideportivo Municipal

-- Insertar datos de prueba en la tabla de eventos
INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, ubicacion, organizador_id, categoria_id, ciudad_id, valor, imagen_html)
VALUES 
('Festival de Verano Familiar', 'Un día lleno de actividades familiares en la playa.', '2024-08-01 10:00:00', '2024-08-01 18:00:00', 'Playa Central', 1, 1, 1, 0.00, '<img src="festival_verano.jpg" />'), -- Evento de verano
('Conferencia de Programación', 'Una conferencia sobre las últimas tendencias en programación.', '2024-08-05 09:00:00', '2024-08-05 17:00:00', 'Sala de Conferencias', 1, 2, 3, 0.00, '<img src="conf_programacion.jpg" />'); -- Evento de programación

-- Insertar datos de prueba en la tabla de inscripciones
INSERT INTO inscripciones (usuario_id, evento_id)
VALUES 
(2, 1), -- Juan Perez inscrito en el Festival de Verano Familiar
(3, 2); -- Maria Garcia inscrita en la Conferencia de Programación

-- Consultas para verificar los datos insertados en las tablas
SELECT * FROM usuarios;
SELECT * FROM categorias;
SELECT * FROM ciudades;
SELECT * FROM eventos;
SELECT * FROM inscripciones;