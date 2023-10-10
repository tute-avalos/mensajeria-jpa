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
package com.tuteavalos.mensajeria.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tuteavalos.mensajeria.model.Mensaje;
import com.tuteavalos.mensajeria.model.Usuario;

import java.util.List;

public interface Cartelera extends JpaRepository<Mensaje, Long> {

    long countByDestinatario(Usuario destinatario);
    List<Mensaje> findAllByDestinatarioOrderByFechaHoraDesc(Usuario destinatario, Pageable pageable);

}
