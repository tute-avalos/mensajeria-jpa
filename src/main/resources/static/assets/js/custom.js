// Copyright (c) 2023 Matías S. Ávalos (@tute_avalos)
// 
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT

jQuery(document).ready(($) => {
  obtener_mensajes();
  $(".popup").hide();
  $(".close").click(function () {
    window.location.reload();
    //$(this).parent().fadeOut("fast");
    //$("table.default").fadeIn("fast");
    //window.location.replace(window.location.href + '#read');
  });
});

function obtener_mensajes(page = 0, size = 10) {
  $.ajax({
    url: `/obtener_mensajes?page=${page}&size=${size}`,
    dataType: 'json',
    success: (mensajes) => {
      let pages = 0;
      $.get('/cantidad_mensajes', (data, textStatus, jqXHR) => {
        pages = Math.ceil(data / 10);
        if (pages != 0) {
          $('#th-pages').html(`Página ${page + 1} de ${pages}`);
        } else {
          $('#th-pages').html('');
        }
        if (pages > 1 && page + 1 != pages) {
          $('#th-pgforward').html(`<span class="icon solid fa-chevron-right"></span>`);
          $('#th-pgforward').click(() => obtener_mensajes(page + 1, 10));
        } else {
          $('#th-pgforward').html('');
        }
        if (page + 1 > 1) {
          $('#th-pgback').html(`<span class="icon solid fa-chevron-left"></span>`);
          $('#th-pgback').click(() => obtener_mensajes(page - 1, 10));
        } else {
          $('#th-pgback').html('');
        }
      });
      // sacar elementos anteriores:
      $('.clickable-row').remove();
      for (const m of mensajes) {
        let row = $(
`<tr class="clickable-row ${!m.leido ? 'unreaded' : ''}" data-msj="${m.id}">
  <td class="col-9">${m.autor.nombres} ${m.autor.apellidos}</td>
  <td class="col-1">${m.fechaHora}</td>
  <td class="col-1"><a class="aln-right" href="/togglear_leido?id=${m.id}"><span id="markread" class="icon solid ${!m.leido ? 'fa-envelope' : 'fa-envelope-open-text'}"></span></a></td>
  <td class="col-1"><a class="aln-right" href="/borrar_mensaje?id=${m.id}"><span id="delete" class="icon solid fa-trash-alt"></span></a></td>
</tr>`);
        $('.table-mensajes').append(row);
      }
      $('.clickable-row').click(function() {
        let id = $(this).data("msj");
        for (const m of mensajes) {
          if (m.id == id) {
            $.get(`/marcar_ledio?id=${m.id}`, () => { });
            $('.mensaje').html(
              `<div class="row">
                <div class="col-12">
                  <h2>${m.autor.nombres} ${m.autor.apellidos}</h2>
                  <h3>${m.fechaHora}</h3>
                  <p>${m.mensaje}</p>
                </div>
              </div>`
            );
            $("table.default").fadeOut('fast');
            $(".popup").fadeIn('fast');
            break;
          }
        }
      });
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert('Error: ' + textStatus + ' - ' + errorThrown);
    }
  });
}


let usr_id = document.getElementById("usr_id");
let email_input = document.getElementById("email_in");
email_input.addEventListener("keyup", (e) => autocomplete(email_input));

function autocomplete(input) {
  removeElements();
  if (input.value != "") {
    const url = `/buscar_clientes/${input.value}`;
    fetch(url)
      .then(response => response.json())
      .then((data) => {
        data.map(function (cliente) {
          let listItem = document.createElement("li");
          //One common class name
          listItem.classList.add("list-items");
          listItem.style.cursor = "pointer";
          listItem.setAttribute("onclick", `setDisplayName('${cliente.email}', ${cliente.id})`);

          let nombres = cliente.nombres.replace(new RegExp(`(^|\\s)(${input.value})`, 'i'), '$1<b>$2</b>');
          let apellidos = cliente.apellidos.replace(new RegExp(`(^|\\s)(${input.value})`, 'i'), '$1<b>$2</b>');
          let email = cliente.email.replace(new RegExp(`(^|\\s)(${input.value})`, 'i'), '$1<b>$2</b>');

          listItem.innerHTML = `${nombres} ${apellidos}<br>${email}`;
          document.querySelector(".list-" + input.getAttribute("name")).appendChild(listItem);
        });
      });
  }
}

function setDisplayName(email, id) {
  email_input.value = email;
  usr_id.value = id;
  removeElements();
}

function removeElements() {
  //clear all the item
  let items = document.querySelectorAll(".list-items");
  items.forEach((item) => item.remove());
}