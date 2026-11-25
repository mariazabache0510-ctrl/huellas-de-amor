document.addEventListener("DOMContentLoaded", () => {
  const revealables = document.querySelectorAll(".card, .stat, .panel, .reveal, .auth-card, .filter-panel");
  if ("IntersectionObserver" in window) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.style.animationPlayState = "running";
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.12 });

    revealables.forEach((el) => {
      el.style.animationPlayState = "paused";
      observer.observe(el);
    });
  }

  const header = document.querySelector(".site-header");
  if (header) {
    const onScroll = () => {
      header.classList.toggle("is-scrolled", window.scrollY > 12);
    };
    onScroll();
    window.addEventListener("scroll", onScroll, { passive: true });
  }

  const toggle = document.querySelector("[data-nav-toggle]");
  const panel = document.querySelector("[data-nav-panel]");
  if (toggle && panel) {
    toggle.addEventListener("click", () => {
      const open = panel.classList.toggle("is-open");
      toggle.setAttribute("aria-expanded", open ? "true" : "false");
      document.body.classList.toggle("nav-open", open);
    });
  }

  const experiencia = document.getElementById("tieneExperiencia");
  const guiaNo = document.getElementById("guiaSinExperiencia");
  const guiaSi = document.getElementById("guiaConExperiencia");
  const syncExperiencia = () => {
    if (!experiencia || !guiaNo || !guiaSi) return;
    const tiene = experiencia.checked;
    guiaSi.hidden = !tiene;
    guiaNo.hidden = tiene;
  };
  if (experiencia) {
    experiencia.addEventListener("change", syncExperiencia);
    syncExperiencia();
  }

  setupConfirmDialog();
});

function setupConfirmDialog() {
  let overlay = document.getElementById("confirmOverlay");
  if (!overlay) {
    overlay = document.createElement("div");
    overlay.id = "confirmOverlay";
    overlay.className = "confirm-overlay";
    overlay.setAttribute("role", "dialog");
    overlay.setAttribute("aria-modal", "true");
    overlay.setAttribute("aria-labelledby", "confirmTitle");
    overlay.innerHTML = `
      <div class="confirm-dialog">
        <div class="confirm-mark" aria-hidden="true">!</div>
        <h2 id="confirmTitle">Confirmar acción</h2>
        <p id="confirmMessage"></p>
        <div class="confirm-actions">
          <button type="button" class="btn btn-secondary" data-confirm-cancel>Cancelar</button>
          <button type="button" class="btn btn-danger" data-confirm-ok>Eliminar</button>
        </div>
      </div>
    `;
    document.body.appendChild(overlay);
  }

  const titleEl = overlay.querySelector("#confirmTitle");
  const messageEl = overlay.querySelector("#confirmMessage");
  const cancelBtn = overlay.querySelector("[data-confirm-cancel]");
  const okBtn = overlay.querySelector("[data-confirm-ok]");
  let pendingForm = null;

  const close = () => {
    overlay.classList.remove("is-open");
    document.body.classList.remove("confirm-open");
    pendingForm = null;
  };

  const open = (form) => {
    pendingForm = form;
    titleEl.textContent = form.dataset.confirmTitle || "Eliminar mascota";
    messageEl.textContent = form.dataset.confirm
      || "¿Eliminar este registro? Esta acción no se puede deshacer.";
    okBtn.textContent = form.dataset.confirmOk || "Eliminar";
    overlay.classList.add("is-open");
    document.body.classList.add("confirm-open");
    okBtn.focus();
  };

  document.addEventListener("submit", (event) => {
    const form = event.target;
    if (!(form instanceof HTMLFormElement) || !form.hasAttribute("data-confirm")) return;
    if (form.dataset.confirmArmed === "1") {
      delete form.dataset.confirmArmed;
      return;
    }
    event.preventDefault();
    open(form);
  });

  cancelBtn.addEventListener("click", close);
  overlay.addEventListener("click", (event) => {
    if (event.target === overlay) close();
  });
  okBtn.addEventListener("click", () => {
    if (!pendingForm) return;
    const form = pendingForm;
    form.dataset.confirmArmed = "1";
    close();
    form.requestSubmit();
  });
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && overlay.classList.contains("is-open")) {
      close();
    }
  });
}
