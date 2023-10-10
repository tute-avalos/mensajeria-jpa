/**
  * Sistema de mensajería con SpringBoot
  * Copyright (C) 2023  Matías S. Ávalos
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Affero General Public License for more details.
  *
  * You should have received a copy of the GNU Affero General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
package com.tuteavalos.mensajeria.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mensajes")
@Data
public class Mensaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id", updatable = false, nullable = false)
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "dest_id", updatable = false, nullable = false)
    private Usuario destinatario;

    @Column(length = 2048, nullable = false)
    private String mensaje;

    @Column(name = "fecha_hora", nullable = false)
    private Timestamp fechaHora;

    @Column(name = "leido", nullable = false)
    private Boolean leido;
}
