const navAccordion = function () {

    const acc = document.getElementsByClassName("nav-acc");

    for (let i = 0; i < acc.length; i++) {

        acc[i].addEventListener("click", function () {
            const chevron = acc[i].lastElementChild;

            /* Toggle between adding and removing the "active" class,
            to highlight the button that controls the panel */
            this.classList.toggle("active");
            chevron.classList.toggle("fa-flip-vertical");

            /* Toggle between hiding and showing the active panel */
            let panel = this.nextElementSibling;
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
            }
        });

    }

};

window.onload = navAccordion;
