const accordion = function (target) {

    const el = document.getElementById(target);

    el.addEventListener("click", function () {
        const chevron = el.lastElementChild;

        this.classList.toggle("active");
        chevron.classList.toggle("fa-flip-vertical");

        let magazine_panel = this.nextElementSibling;

        if (magazine_panel.style.maxHeight) magazine_panel.style.maxHeight = null;
        else magazine_panel.style.maxHeight = magazine_panel.scrollHeight + "px";
    });

};
