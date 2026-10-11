-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema Vuelta_al_tablero_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Vuelta_al_tablero_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Vuelta_al_tablero_db` DEFAULT CHARACTER SET utf8mb4 ;
USE `Vuelta_al_tablero_db` ;

-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`usuario` (
  `nro` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `dni` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `contrasenia` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(15) NOT NULL,
  `fecha_registro` DATE NOT NULL DEFAULT (CURRENT_DATE()),
  `rol_admin` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`nro`),
  UNIQUE INDEX `dni_UNIQUE` (`dni` ASC),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC),
  UNIQUE INDEX `contrasenia_UNIQUE` (`contrasenia` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`ficha_tecnica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`ficha_tecnica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `max_jugadores` INT NULL,
  `min_jugadores` INT NULL,
  `min_edad` INT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `anio_publicacion` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`juego`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`juego` (
  `id` INT NOT NULL,
  `titulo` VARCHAR(45) NOT NULL,
  `editorial` VARCHAR(25) NOT NULL,
  `categoria` VARCHAR(25) NOT NULL,
  `id_ficha_tecnica` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_juego_ficha_tecnica1_idx` (`id_ficha_tecnica` ASC),
  CONSTRAINT `fk_juego_ficha_tecnica`
    FOREIGN KEY (`id_ficha_tecnica`)
    REFERENCES `Vuelta_al_tablero_db`.`ficha_tecnica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`ejemplar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`ejemplar` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `estado` VARCHAR(25) NOT NULL DEFAULT 'Nuevo',
  `disponible_alquiler` TINYINT NOT NULL DEFAULT 0,
  `id_juego` INT NOT NULL,
  `nro_usuario` INT NOT NULL,
  PRIMARY KEY (`id`, `id_juego`, `nro_usuario`),
  INDEX `fk_ejemplar_juego1_idx` (`id_juego` ASC),
  INDEX `fk_ejemplar_usuario1_idx` (`nro_usuario` ASC),
  CONSTRAINT `fk_ejemplar_juego`
    FOREIGN KEY (`id_juego`)
    REFERENCES `Vuelta_al_tablero_db`.`juego` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ejemplar_usuario`
    FOREIGN KEY (`nro_usuario`)
    REFERENCES `Vuelta_al_tablero_db`.`usuario` (`nro`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`alquila`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`alquila` (
  `nro_usuario` INT NOT NULL,
  `id_ejemplar` INT NOT NULL,
  `precio` DOUBLE NOT NULL,
  `fecha_entrega` DATE NULL,
  `fecha_devolucion` DATE NULL,
  PRIMARY KEY (`nro_usuario`, `id_ejemplar`),
  INDEX `fk_usuario_has_ejemplar_ejemplar1_idx` (`id_ejemplar` ASC),
  INDEX `fk_usuario_has_ejemplar_usuario1_idx` (`nro_usuario` ASC),
  CONSTRAINT `fk_alquiler_usuario`
    FOREIGN KEY (`nro_usuario`)
    REFERENCES `Vuelta_al_tablero_db`.`usuario` (`nro`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ejemplar_alquiler`
    FOREIGN KEY (`id_ejemplar`)
    REFERENCES `Vuelta_al_tablero_db`.`ejemplar` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`critica_ejemplar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`critica_ejemplar` (
  `id_ejemplar` INT NOT NULL,
  `nro_usuario` INT NOT NULL,
  `comentario` VARCHAR(200) NULL,
  `puntuacion` INT NOT NULL,
  PRIMARY KEY (`id_ejemplar`, `nro_usuario`),
  INDEX `fk_ejemplar_has_usuario_usuario1_idx` (`nro_usuario` ASC),
  INDEX `fk_ejemplar_has_usuario_ejemplar1_idx` (`id_ejemplar` ASC),
  CONSTRAINT `fk_ejemplar`
    FOREIGN KEY (`id_ejemplar`)
    REFERENCES `Vuelta_al_tablero_db`.`ejemplar` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_critica2_usuario`
    FOREIGN KEY (`nro_usuario`)
    REFERENCES `Vuelta_al_tablero_db`.`usuario` (`nro`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vuelta_al_tablero_db`.`critica_juego`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vuelta_al_tablero_db`.`critica_juego` (
  `nro_usuario` INT NOT NULL,
  `id_juego` INT NOT NULL,
  `comentario` VARCHAR(200) NULL,
  `puntuacion` INT NOT NULL,
  PRIMARY KEY (`nro_usuario`, `id_juego`),
  INDEX `fk_usuario_has_juego_juego2_idx` (`id_juego` ASC),
  INDEX `fk_usuario_has_juego_usuario2_idx` (`nro_usuario` ASC),
  CONSTRAINT `fk_critica1_usuario`
    FOREIGN KEY (`nro_usuario`)
    REFERENCES `Vuelta_al_tablero_db`.`usuario` (`nro`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_juego`
    FOREIGN KEY (`id_juego`)
    REFERENCES `Vuelta_al_tablero_db`.`juego` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;