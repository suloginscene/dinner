window.addEventListener('load', function () {

    validate('#signup-form');

    const form = document.getElementById('signup-form');
    const term = document.getElementById('term');
    const termHint = document.getElementById('term-hint');

    const checkTerm = function (e) {
        if (!term.checked) {
            e.preventDefault();
            termHint.style.display = 'block';
        } else {
            termHint.style.display = 'none';
        }
    };


    form.addEventListener('submit', checkTerm, false);
    term.addEventListener('input', checkTerm, false);

});
