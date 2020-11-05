const scrollSensibleNavbar = function () {

    let scrolled = false;
    let hidden = false;
    let last = 0;
    const $navbar = $('.navbar');
    const $dropdown = $('#account-dropdown');

    const scrollSensor = function () {
        scrolled = true;
    };

    const scrollHandler = function () {
        if (!scrolled) return;
        scrolled = false;

        const top = window.scrollY;
        const scrollDown = (last < top);
        last = top;
        if (scrollDown === hidden) return;

        $dropdown.removeClass('opened');
        $navbar.toggleClass('scroll-down');
        hidden = !hidden;
    };


    window.addEventListener('scroll', scrollSensor);
    setInterval(scrollHandler, 250);

};

window.addEventListener('load', scrollSensibleNavbar);
