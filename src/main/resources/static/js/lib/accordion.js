const accordion = function (accordion) {

    const $accordion = $(accordion);
    const $chevron = $accordion.children('i');
    const $panel = $accordion.next();

    const open = function () {
        $panel.css('max-height', $panel.prop('scrollHeight'));
    }

    const close = function () {
        $panel.css('max-height', 0);
    }

    const toggle = function () {

        $accordion.toggleClass("active");
        $chevron.toggleClass("fa-flip-vertical");

        const closed = $panel.css('max-height') === '0px';
        closed ? open() : close();

    };


    $accordion.bind("click", toggle);

};
