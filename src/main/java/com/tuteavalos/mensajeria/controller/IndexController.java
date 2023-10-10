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
package com.tuteavalos.mensajeria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tuteavalos.mensajeria.model.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

    @GetMapping("/")
    public String homeDispatcher(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("user");
        if(u == null) return "login";
        model.addAllAttributes(u.obtenerElementosPlantilla());
        return u.obtenerNombrePlantilla();
    }
}
