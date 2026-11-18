document.addEventListener("DOMContentLoaded", () => {
  const cards = document.querySelectorAll(".card, .stat, .panel");
  if ("IntersectionObserver" in window) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.style.animationPlayState = "running";
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.12 });

    cards.forEach((el) => {
      el.style.animationPlayState = "paused";
      observer.observe(el);
    });
  }
});
