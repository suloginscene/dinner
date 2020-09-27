const magazineAccordion = function () {

    const introduction = document.getElementById("introduction");

    introduction.addEventListener("click", function () {
        const magazine_chevron = introduction.lastElementChild;

        this.classList.toggle("active");
        magazine_chevron.classList.toggle("fa-flip-vertical");

        let magazine_panel = this.nextElementSibling;
        if (magazine_panel.style.maxHeight) {
            magazine_panel.style.maxHeight = null;
        } else {
            magazine_panel.style.maxHeight = magazine_panel.scrollHeight + "px";
        }
    });

};

window.onload = function () {
    navAccordion();
    magazineAccordion();
}