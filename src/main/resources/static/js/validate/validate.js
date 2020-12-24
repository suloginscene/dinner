const validate = function (form) {

    const $form = $(form);

    const checkStrings = function (e) {
        if ($form[0].checkValidity() === false) {
            e.preventDefault();
            e.stopPropagation();
        }
        $form.addClass('was-validated');
    };


    // TODO use after refine article validation
    // $form.bind('input', checkStrings);
    $form.bind('submit', checkStrings);

};
