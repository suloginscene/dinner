const accounts = function () {

    const $dropdown = $('#account-dropdown');

    const open = function () {
        $dropdown.addClass('opened');
    };

    const close = function () {
        $dropdown.removeClass('opened');
    };


    $dropdown.bind('mouseenter', open);
    $dropdown.bind('touchstart', open);
    $dropdown.bind('mouseleave', close);

};

window.addEventListener('load', accounts);
