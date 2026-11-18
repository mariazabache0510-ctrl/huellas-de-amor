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
