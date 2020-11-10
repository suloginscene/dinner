const ajaxMagazines = function () {

    const loadMagazinesOnNav = function (magazineArr) {
        const template = document.querySelector("#magazine-template").innerHTML;

        const $magazineStackBase = $('#magazine-list');
        magazineArr.forEach(function (m) {
            const el = template.replace("{id}", m.id).replace("{title}", m.title);
            $magazineStackBase.before(el);
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/magazines',
        success: loadMagazinesOnNav,
    });

};

window.addEventListener('load', ajaxMagazines);
