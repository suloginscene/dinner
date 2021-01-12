const validate = function (form) {

    const $form = $(form);

    const checkStrings = function (e) {
        if ($form[0].checkValidity() === false) {
            e.preventDefault();
            e.stopPropagation();
        }
        $form.addClass('was-validated');
    };


    $form.bind('submit', checkStrings);

};
