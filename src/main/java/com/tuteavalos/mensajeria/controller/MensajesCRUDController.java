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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.tuteavalos.mensajeria.model.Mensaje;
import com.tuteavalos.mensajeria.model.Usuario;
import com.tuteavalos.mensajeria.repository.Cartelera;
import com.tuteavalos.mensajeria.repository.RepoDeClientes;

import jakarta.servlet.http.HttpSession;

@Controller
public class MensajesCRUDController {
    @Autowired
    private Cartelera cartelera;
    @Autowired
    private RepoDeClientes repoClientes;

    @PostMapping("/nuevo_mensaje")
    public RedirectView registrarNuevoMensaje(@RequestParam("usr_id") Long usr_id, @RequestParam("mensaje") String msj,
            RedirectAttributes attributes, HttpSession session) {
        Usuario autor = (Usuario) session.getAttribute("user");
        Usuario destinatario = repoClientes.findById(usr_id).orElse(null);
        if (destinatario != null) {
            Mensaje m = new Mensaje();
            m.setAutor(autor);
            m.setDestinatario(destinatario);
            m.setMensaje(msj);
            m.setFechaHora(Timestamp.from(Instant.now()));
            m.setLeido(false);
            m = cartelera.saveAndFlush(m);
            attributes.addFlashAttribute("successMsj", "Mensaje enviado con éxito.");
        } else {
            attributes.addFlashAttribute("errorMsj", "ERROR: No existe el destinatario.");
        }
        return new RedirectView("/#write");
    }

    @GetMapping("/cantidad_mensajes")
    @ResponseBody
    public long obtenerCantidadDeMensajes(HttpSession session) {
        Usuario destinatario = (Usuario) session.getAttribute("user");
        return cartelera.countByDestinatario(destinatario);
    }

    @GetMapping("/obtener_mensajes")
    @ResponseBody
    public List<Mensaje> obtenerMensajesPorPagina(@RequestParam("page") Integer page,
            @RequestParam("size") Integer size, HttpSession session) {
        Usuario destinatiario = (Usuario) session.getAttribute("user");
        List<Mensaje> mensajes = cartelera.findAllByDestinatarioOrderByFechaHoraDesc(destinatiario,
                PageRequest.of(page, size));
        return mensajes != null ? mensajes : new ArrayList<>();
    }

    @GetMapping("/borrar_mensaje")
    public RedirectView borrarMensaje(@RequestParam("id") Long id, HttpSession session) {
        Usuario destinatario = (Usuario) session.getAttribute("user");
        Mensaje m = cartelera.findById(id).orElse(null);
        if(m != null && m.getDestinatario().equals(destinatario)) {
            cartelera.deleteById(id);
            cartelera.flush();
        }
        return new RedirectView("/#read");
    }

    @GetMapping("/togglear_leido")
    public RedirectView togglearLeido(@RequestParam("id") Long id, HttpSession session) {
        Usuario destinatario = (Usuario) session.getAttribute("user");
        Mensaje m = cartelera.findById(id).orElse(null);
        if(m != null && m.getDestinatario().equals(destinatario)) {
            m.setLeido(!m.getLeido());
            cartelera.saveAndFlush(m);
        }
        return new RedirectView("/#read");
    }

    @GetMapping("/marcar_ledio")
    @ResponseBody
    public Boolean marcarLeido(@RequestParam("id") Long id, HttpSession session) {
        Usuario destinatario = (Usuario) session.getAttribute("user");
        Mensaje m = cartelera.findById(id).orElse(null);
        if(m != null && m.getDestinatario().equals(destinatario)) {
            m.setLeido(true);
            cartelera.saveAndFlush(m);
            return true;
        }
        return false;
    }

}
