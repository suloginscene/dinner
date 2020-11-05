const dropdown = function (target) {

    const $dropdown = $(target);

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
