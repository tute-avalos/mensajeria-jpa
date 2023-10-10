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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.tuteavalos.mensajeria.model.Usuario;
import com.tuteavalos.mensajeria.repository.RepoDeUsuarios;

import jakarta.servlet.http.HttpSession;

@Controller
public class SesionController {
    @Autowired
    private RepoDeUsuarios repoUsuarios;

    @PostMapping("/login")
    public RedirectView login(@RequestParam("usr") String usr, @RequestParam("pswd") String pswd,
            RedirectAttributes attributes, HttpSession session) {
        Usuario u = repoUsuarios.findByUsernameAndContrasenya(usr, pswd);
        if (u == null) {
            attributes.addFlashAttribute("error", "Usuario y/o Contraseña incorrecta.");
        } else {
            session.setAttribute("user", u);
        }
        return new RedirectView("/");
    }

    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET,RequestMethod.POST})
    public RedirectView logout(RedirectAttributes attributes, HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }
}
