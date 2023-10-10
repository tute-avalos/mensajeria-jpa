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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.tuteavalos.mensajeria.model.Cliente;
import com.tuteavalos.mensajeria.model.Usuario;
import com.tuteavalos.mensajeria.repository.RepoDeClientes;
import com.tuteavalos.mensajeria.repository.RepoDeUsuarios;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuariosCRUDController {
    @Autowired
    private RepoDeUsuarios repoUsuarios;
    @Autowired
    private RepoDeClientes repoClientes;

    @PostMapping("/adduser")
    public RedirectView registrarNuevoCliente(
            @ModelAttribute Cliente nuevoUsuario,
            RedirectAttributes attributes) {
        Usuario u = null;
        try {
            u = repoUsuarios.save(nuevoUsuario);
        } catch (DataIntegrityViolationException ex) {
            String error = ex.getMessage();
            if (error.contains("Duplicate")) {
                String valorDuplicado = error.substring(error.indexOf("entry") + 6, error.indexOf("for") - 1);
                attributes.addFlashAttribute("errorMsj", "Valor duplicado " + valorDuplicado + ".");
            } else {
                attributes.addFlashAttribute("errorMsj", "Error de ingreso.");
            }
        } finally {
            if (u != null) {
                attributes.addFlashAttribute("successMsj", "Usuario " + u.getUsername() + " creado con éxito.");
            }
        }
        return new RedirectView("/#nuevoUsuario");
    }

    @GetMapping("/buscar_clientes/{query}")
    @ResponseBody
    public List<Cliente> buscarClientesPorNombres(@PathVariable String query) {
        query = query == null ? "" : query;
        Set<Cliente> c = new HashSet<>();
        c.addAll(repoClientes.findByNombresStartingWithIgnoreCaseOrderByNombres(query));
        c.addAll(repoClientes.findByApellidosStartingWithIgnoreCaseOrderByApellidos(query));
        c.addAll(repoClientes.findByEmailStartingWithIgnoreCaseOrderByEmail(query));
        return new ArrayList<>(c);
    }

    @PostMapping("/cambiar_password")
    public RedirectView cambiarPassword(@RequestParam("pswd_old") String viejo, @RequestParam("pswd_new") String nuevo,
            RedirectAttributes attributes, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("user");
        if(u != null && u.getContrasenya().equals(viejo)) {
            u.setContrasenya(nuevo);
            repoUsuarios.saveAndFlush(u);
            attributes.addFlashAttribute("successPswd", "Contraseña cambiada con éxito.");
        } else {
            attributes.addFlashAttribute("errorPswd", "Error en el cambio de contraseña.");
        }
        return new RedirectView("/");
    }

}
