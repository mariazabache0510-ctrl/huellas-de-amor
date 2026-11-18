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

  document.querySelectorAll(".btn, .card, .auth-demo span").forEach((el) => {
    el.addEventListener("mousemove", (event) => {
      const rect = el.getBoundingClientRect();
      const x = ((event.clientX - rect.left) / rect.width - 0.5) * 6;
      const y = ((event.clientY - rect.top) / rect.height - 0.5) * 6;
      el.style.translate = `${x}px ${y}px`;
    });
    el.addEventListener("mouseleave", () => {
      el.style.translate = "0px 0px";
    });
  });
});
